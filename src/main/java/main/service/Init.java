package main.service;

import main.AnotherSearchEngine;
import main.config.Config;
import main.repository.entity.Site;
import main.repository.entity.SiteStatus;
import main.repository.entity.Field;
import main.service.db_service.FieldService;
import main.service.db_service.SiteService;
import main.service.site_map_and_index.interfaces.SiteMappingAndIndexingProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Init
{
    @Autowired
    FieldService fieldService;

    @Autowired
    SiteService siteService;

    @Autowired
    Config config;

    @Autowired
    SiteMappingAndIndexingProcessor siteMappingAndIndexingProcessor;

    private static final String DB_MODE_PROP = "spring.jpa.hibernate.ddl-auto";
    private static final String INIT_DB_MODE = "create";


    /**
     * проверка необходимости заполнения таблицы field
     * проверка наличия в конфигурационном файле добавленных / удалёных сайтов
     */
    public void run()
    {
        String dbMode = AnotherSearchEngine.getContext().getEnvironment().getProperty(DB_MODE_PROP);

        if(dbMode != null && dbMode.equalsIgnoreCase(INIT_DB_MODE))
        {
            tableFieldFilling();
        }

        verifyConfigVsDbSites();

        System.out.println("Application is running...");
    }

    /**
     * заполнение таблицы, содержащей информацию о полях страниц, в которых будет производиться поиск лемм
     * (заполнение таблицы field)
     */
    private void tableFieldFilling()
    {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field("title", "title", 1.0f));
        fields.add(new Field("body", "body", 0.8f));
        fieldService.saveAll(fields);
    }

    /**
     * сравнение списка сайтов, находящися в конфигурационном файле с БД;
     * добавление / удаление информации о сайтах из БД при расхождениях
     */
    private void verifyConfigVsDbSites()
    {
        List<Site> sitesToRemove = new ArrayList<>();

        Map<String, String> sitesFromConfig = getSitesFromConfig();

        Iterable<Site> sitesFromDb = siteService.findAll();

        for(Site curSite : sitesFromDb)
        {
            String curSiteUrl = curSite.getUrl();

            if(sitesFromConfig.containsKey(curSiteUrl))
            {
                sitesFromConfig.remove(curSiteUrl);
            }
            else
            {
                sitesToRemove.add(curSite);
            }
        }

        saveAddedSites(sitesFromConfig);
        removeConfigAbsentSites(sitesToRemove);
    }

    /**
     * чтение информации о сайтах из конфигурационного файла
     * @return Map: key = ссылка на сайт; value = имя сайта
     */
    private Map<String, String> getSitesFromConfig()
    {
        Map<String, String> sitesFromConfig = new HashMap<>();

        List<Map<String, String>> sitesData = config.getSites();

        for(Map<String, String> curSiteData : sitesData)
        {
            String siteUrl;
            String tempUrl = curSiteData.get("url");

            if (!tempUrl.endsWith("/"))
            {
                siteUrl = tempUrl.concat("/");

            }
            else
            {
                siteUrl = tempUrl;
            }

            String siteName = curSiteData.get("name");

            sitesFromConfig.put(siteUrl, siteName);
        }

        return sitesFromConfig;
    }

    /**
     * сохранение в БД добавленных сайтов (присутствующих в конфигурационном файле и отсутствующих в БД)
     * @param newSitesFromConfig сайты, присутствующие в конфигурационном файле и отсутствующие в БД
     */
    private void saveAddedSites(Map<String, String> newSitesFromConfig)
    {
        List<Site> sitesToSave = new ArrayList<>();

        for(String curSiteUrl : newSitesFromConfig.keySet())
        {
            String siteName = newSitesFromConfig.get(curSiteUrl);
            Site newSite = new Site(SiteStatus.INDEXING, LocalDateTime.now(), null, curSiteUrl, siteName);
            sitesToSave.add(newSite);
        }

        siteService.saveAll(sitesToSave);
    }

    /**
     * удаление из БД данных, относящихся к сайтам, присутствующим в БД, но отсутствующим в конфигурационном файле
     * @param sitesToRemove сайты, присутствующие в БД, но отсутствующие в конфигурационном файле
     */
    private void removeConfigAbsentSites(List<Site> sitesToRemove)
    {
        for(Site curSite : sitesToRemove)
        {
            siteMappingAndIndexingProcessor.removeSiteData(curSite);
            siteService.delete(curSite);
        }
    }
}