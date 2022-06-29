package main.service.app_control;

import main.controller.responses.Response;

/**
 * Интерфейс, использующийся для запуска и остановки процесса индексации всех сайтов из конфигурационного файла
 */
public interface SitesIndexingMainProcessor
{
    /**
     * Запуск индексации выбранного сайта / всех сайтов из конфигурационного файла
     * @param siteUrl ссылка на сайт. Если null, то индексируются все сайты
     * @return true, если новый процесс индексации была запущена; false, если ещё не закончен текущий процесс индексации
     */
    Response launchSitesIndexing(String siteUrl);

    /**
     * Остановка процесса индексации
     * @return true, если текущая индексация была остановлена; false, если процесс индексации не был запущен
     */
    Response stopSitesIndexing();

    /**
     * Метод определяет, есть ли незавершённые задачи в списке задач по индексации сайтов (indexingFutureList)
     * @return true, если в indexingFutureList есть незавершённые задачи; false в обратном случае
     */
    boolean isIndexingInProgress();

    /**
     * Запуск добавления или обновления отдельной страницы
     * @param pageUrl ссылка на страницу
     * @return Response со значением true, если страница была успешно обновлена или добавлена;
     * со значением false, если в процессе произошла ошибка
     */
    Response singlePageIndexing(String pageUrl);

}
