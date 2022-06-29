package main.service.search_system;

import main.controller.responses.Response;

/**
 * интерфейс, использующийся для получения ответа на поисковый запрос от пользователя
 */
public interface SiteSearchProcessor
{
    /**
     * обработка поискового запроса от пользователя
     * @param queryText поисковый запрос
     * @param siteUrl ссылка на сайт, по которому необходимо осуществить поиск
     * @param resultsQtyLimit количество результатов, которое необходимо вывести
     * @return объект класса response, содержащий результат поиска
     */
    Response searchSites(String queryText, String siteUrl, int resultsQtyLimit);
}
