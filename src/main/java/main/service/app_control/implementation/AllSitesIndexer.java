package main.service.app_control.implementation;

import main.AnotherSearchEngine;
import main.controller.responses.Response;
import main.controller.responses.ResponseFail;
import main.controller.responses.ResponseSuccess;
import main.repository.entity.Site;
import main.service.app_control.SitesIndexingMainProcessor;
import main.service.db_service.SiteService;
import main.service.site_map_and_index.interfaces.SiteMappingAndIndexingProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Класс, использующийся для запуска и остановки процесса индексации всех сайтов из конфигурационного файла
 */
@Component
class AllSitesIndexer implements SitesIndexingMainProcessor
{
    @Autowired
    SiteService siteService;

    @Autowired
    ApplicationContext context = AnotherSearchEngine.getContext();

    List<Future<?>> indexingFutureList;

    ExecutorService executor;

    List<SiteMappingAndIndexingProcessor> siteProcessorList;


    /**
     * Запуск индексации выбранного сайта / всех сайтов из конфигурационного файла
     * @param siteUrl ссылка на сайт. Если null, то индексируются все сайты
     * @return объект Response со значением true, если новый процесс индексации был запущен;
     * со значением false, если ещё не закончен текущий процесс индексации
     */
    @Override
    public Response launchSitesIndexing(String siteUrl)
    {
        Response response;

        if(!isIndexingInProgress())
        {
            indexingFutureList = new ArrayList<>();
            siteProcessorList = new ArrayList<>();
            executor = Executors.newCachedThreadPool();

            Iterable<Site> sites = getSitesForIndexing(siteUrl);

            for (Site curSite : sites)
            {
                SiteMappingAndIndexingProcessor siteMappingAndIndexingProcessor =
                        context.getBean(SiteMappingAndIndexingProcessor.class);

                siteMappingAndIndexingProcessor.removeSiteData(curSite);

                siteProcessorList.add(siteMappingAndIndexingProcessor);

                Runnable indexingRunnable = () -> siteMappingAndIndexingProcessor.getAndIndexPages(curSite);

                Future <?> indexingFuture = executor.submit(indexingRunnable);

                indexingFutureList.add(indexingFuture);
            }

            executor.shutdown();

            response = new ResponseSuccess(true);

        }
        else
        {
            response = new ResponseFail(false, "Индексация уже запущена");
        }

        return response;
    }

    /**
     * Остановка процесса индексации
     * @return Response со значением true, если текущая индексация была остановлена;
     * со значением false, если процесс индексации не был запущен
     */
    @Override
    public Response stopSitesIndexing()
    {
        Response response;
        int attemptsQty = 0;
        int maxAttempts = 40;

        if(isIndexingInProgress())
        {
            //Profiling
            long start = System.currentTimeMillis();

            for(SiteMappingAndIndexingProcessor siteProcessor : siteProcessorList)
            {
                try
                {
                    siteProcessor.terminate();
                }
                catch (Exception ex)
                {

                }
            }

            attemptsQty = waitForCompletion(500, maxAttempts);

            if(attemptsQty < maxAttempts)
            {
                //Profiling
                long end = System.currentTimeMillis();
                System.out.println("Terminated " + (end - start) + " ms");

                response = new ResponseSuccess(true);
            }
            else
            {
                response = new ResponseFail(false, "Не удалось остановить индексацию");
            }
        }
        else
        {
            response = new ResponseFail(false, "Индексация не запущена");
        }

        return response;
    }

    /**
     * Метод определяет, есть ли незавершённые задачи в списке задач по индексации сайтов (indexingFutureList)
     * @return true, если в indexingFutureList есть незавершённые задачи; false в обратном случае
     */
    @Override
    public boolean isIndexingInProgress()
    {
        if(indexingFutureList == null)
        {
            return false;
        }

        long activeTasksQty = indexingFutureList.stream().filter(future -> !future.isDone()).count();

        return activeTasksQty > 0;
    }

    /**
     * Запуск добавления или обновления отдельной страницы
     * @param pageUrl ссылка на страницу
     * @return Response со значением true, если страница была успешно обновлена или добавлена;
     * со значением false, если в процессе произошла ошибка
     */
    @Override
    public Response singlePageIndexing(String pageUrl)
    {
        Response response;

        if(!isIndexingInProgress())
        {
            indexingFutureList = new ArrayList<>();
            executor = Executors.newCachedThreadPool();
            SiteMappingAndIndexingProcessor siteMappingAndIndexingProcessor =
                context.getBean(SiteMappingAndIndexingProcessor.class);

            Callable<Integer> indexingCallable = () -> siteMappingAndIndexingProcessor.indexSinglePage(pageUrl);
            Future<Integer> indexingFuture = executor.submit(indexingCallable);
            indexingFutureList.add(indexingFuture);
            executor.shutdown();
            response = getPageIndexingResponse(indexingFuture);
        }
        else
        {
            response = new ResponseFail(false, "Индексация не запущена");
        }


        return response;
    }

    /**
     * Проверка, осуществляется ли процесс индексации и приостановка главного потока
     * @param sleepTime время в мс
     * @param maxAttempts максимальное количество остановок
     * @return количество остановок потока
     */
    private int waitForCompletion(long sleepTime, int maxAttempts)
    {
        int attemptsQty = 0;
        while(isIndexingInProgress() && attemptsQty < maxAttempts)
        {
            try
            {
                Thread.sleep(sleepTime);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            ++attemptsQty;
        }
        return attemptsQty;
    }

    /**
     * Получение результата добавления или обновления отдельной страницы и создание объекта Response
     * @param indexingFuture объект Future
     * @return Response со значением true, если страница была успешно обновлена или добавлена;
     * со значением false, если в процессе произошла ошибка
     */
    private Response getPageIndexingResponse(Future<Integer> indexingFuture)
    {
        int maxAttempts = 150;
        Response response = new ResponseFail(false, "Не удалось завершить индексацию страницы");;

        int attemptsQty = waitForCompletion(2000, maxAttempts);

        if(attemptsQty < maxAttempts)
        {
            try
            {
                int result = indexingFuture.get();

                if(result == 1)
                {
                    response = new ResponseSuccess(true);
                }
                else if(result == 0)
                {
                    response = new ResponseFail(false,
                            "Данная страница находится за пределами сайтов, указанных в конфигурационном файле");
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        return response;
    }

    /**
     * Получение объекта / объектов Site по ссылке
     * @param siteUrl ссылка на сайт
     * @return объект / объекты Site. Если входящая ссылка = null, то возвращаются все сайты из БД
     */
    private Iterable<Site> getSitesForIndexing(String siteUrl)
    {
        Iterable<Site> sites;
        if(siteUrl == null)
        {
            sites = siteService.findAll();
        }
        else
        {
            String urlForSearch;
            if(siteUrl.endsWith("/"))
            {
                urlForSearch = siteUrl;
            }
            else
            {
                urlForSearch = siteUrl.concat("/");
            }
            sites = siteService.findByUrl(urlForSearch);
        }
        return sites;
    }
}