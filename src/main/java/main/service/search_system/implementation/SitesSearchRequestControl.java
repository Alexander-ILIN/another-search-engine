package main.service.search_system.implementation;

import main.controller.responses.Response;
import main.controller.responses.ResponseFail;
import main.controller.responses.ResponseSearch;
import main.service.search_system.SearchResultProcessor;
import main.service.search_system.SiteSearchProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * класс, реализующий получение ответа на поисковый запрос от пользователя
 */
@Component
class SitesSearchRequestControl implements SiteSearchProcessor
{
    @Autowired
    SearchResultProcessor searchResultProcessor; // объект, осуществляющий выполнение поискового запроса

    /**
     * обработка поискового запроса от пользователя
     * @param queryText поисковый запрос
     * @param siteUrl ссылка на сайт, по которому необходимо осуществить поиск
     * @param resultsQtyLimit количество результатов, которое необходимо вывести
     * @return объект класса response, содержащий результат поиска
     */
    @Override
    public Response searchSites(String queryText, String siteUrl, int resultsQtyLimit)
    {
        Response response;

        try
        {
            SearchResultData[] resultData = searchResultProcessor.getSearchResults(queryText, siteUrl, resultsQtyLimit);
            response = new ResponseSearch(true, resultData.length, resultData);
        }
        catch (IOException ioEx)
        {
            response = new ResponseFail(false, ioEx.getMessage());
        }
        catch (Exception ex)
        {
            response = new ResponseFail(false, "Не удалось совершить поиск");
        }

        return response;
    }

}
