package main.service.site_map_and_index.implementation;

import main.service.app_control.SitesIndexingMainProcessor;
import main.controller.responses.Response;
import main.controller.responses.ResponseStatistics;
import main.repository.entity.Site;
import main.service.db_service.LemmaService;
import main.service.db_service.PageService;
import main.service.db_service.SiteService;
import main.service.site_map_and_index.interfaces.StatisticsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * класс, использующийся для сбора статистики по индексации сайтов: общей и по каждому сайту
 */
@Component
class StatisticsCollector implements StatisticsProcessor
{
    @Autowired
    SiteService siteService;

    @Autowired
    PageService pageService;

    @Autowired
    LemmaService lemmaService;

    @Autowired
    SitesIndexingMainProcessor sitesIndexingMainProcessor;

    /**
     * сбор статистики индексации: общей и по каждому сайту
     * @return объект Response, содержащий в себе объект StatisticsProcessor
     */
    @Override
    public Response getStatistics()
    {
        long sitesQty = siteService.count();
        long pagesQty = pageService.count();
        long lemmasQty = lemmaService.count();
        boolean isIndexing = sitesIndexingMainProcessor.isIndexingInProgress();

        Statistics statistics = new Statistics(sitesQty, pagesQty, lemmasQty, isIndexing);

        Iterable<Site> sites = siteService.findAll();

        for(Site curSite : sites)
        {
            int siteId = curSite.getId();
            long sitePagesQty = pageService.countAllBySiteId(siteId);
            long siteLemmasQty = lemmaService.countAllBySiteId(siteId);

            statistics.addDetailedInfo(curSite, sitePagesQty, siteLemmasQty);
        }

        Response responseStatistics = new ResponseStatistics(true, statistics);

        return responseStatistics;
    }
}