package main.controller;

import main.service.search_system.SiteSearchProcessor;
import main.service.app_control.SitesIndexingMainProcessor;
import main.service.site_map_and_index.interfaces.StatisticsProcessor;
import main.controller.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Класс контроллера
 */
@RestController
public class AppController
{
    @Autowired
    SitesIndexingMainProcessor sitesIndexingMainProcessor;

    @Autowired
    StatisticsProcessor statisticsProcessor;

    @Autowired
    SiteSearchProcessor siteSearchProcessor;


    @GetMapping(value = "/search")
    public ResponseEntity<Response> search(@RequestParam(name = "query") String queryText,
                                 @RequestParam(required = false, name = "site") String siteUrl,
                                 @RequestParam(name = "offset") int outputOffset,
                                 @RequestParam(name = "limit") int resultsQtyLimit)
    {
        Response searchResponse;

        searchResponse = siteSearchProcessor.searchSites(queryText, siteUrl, resultsQtyLimit);

        return ResponseEntity.status(HttpStatus.OK).body(searchResponse);
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<Response> startIndexing(@RequestParam(required = false, name = "site") String siteUrl)
    {
        Response responseStartIndexing = sitesIndexingMainProcessor.launchSitesIndexing(siteUrl);

        return ResponseEntity.status(HttpStatus.OK).body(responseStartIndexing);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<Response> stopIndexing()
    {
        Response responseStopIndexing = sitesIndexingMainProcessor.stopSitesIndexing();

        return ResponseEntity.status(HttpStatus.OK).body(responseStopIndexing);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Response> getStatistics()
    {
        Response responseStatistics = statisticsProcessor.getStatistics();

        return ResponseEntity.status(HttpStatus.OK).body(responseStatistics);
    }

    @PostMapping("/indexPage")
    public ResponseEntity<Response> indexPage(@RequestParam(name = "url") String pageUrl)
    {
        Response responseIndexPage = sitesIndexingMainProcessor.singlePageIndexing(pageUrl);

        return ResponseEntity.status(HttpStatus.OK).body(responseIndexPage);
    }
}
