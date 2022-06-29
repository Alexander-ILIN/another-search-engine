package main.service.search_system.implementation;

import main.repository.entity.SearchIndex;
import main.repository.entity.Site;
import org.jsoup.nodes.Document;
import java.util.*;

/**
 * класс, используемый для генерации и хранения данных для ответа на поисковый запрос
 */
class SearchResult
{
    private static List<SearchResult> searchResultList = new ArrayList<>();             // список всех созданных объектов класса SearchResult для всех сайтов
    private static Map<Integer, SearchResult> siteSearchResultMap = new HashMap<>();    // Map, Key = id страницы, value = объект класса SearchResult, относящийся к странице для одного сайта
    private static float maxRelevance = 0f;     // максимальная относительная релевантность
    private List<SearchIndex> searchIndexList;  // список объектов поисковых индексов
    private final int pageId;                   // id страницы, к которой относится данный объект класса SearchResult
    private float absRelevance;                 // абсолютная релевантность страницы
    private float relRelevance;                 // относительная релевантность страницы
    private String title;                       // заголовок страницы
    private String snippet;                     // фрагмент текста, в котором найдены совпадения
    private String siteUrl;                     // ссылка на сайт, к которому относится страница
    private String siteName;                    // имя сайта, к которому относится страница
    private String pageUrl;                     // ссылка на страницу
    private Document htmlDocument;              // содержимое страницы


    /**
     * конструктор класса
     * @param site сайт
     * @param pageId ID страницы
     * @param searchIndex поисковый индекс
     */
    public SearchResult(Site site, int pageId, SearchIndex searchIndex)
    {
        this.pageId = pageId;
        this.siteUrl = site.getUrl();
        this.siteName = site.getName();
        searchIndexList = new ArrayList<>();
        searchIndexList.add(searchIndex);

    }

    /**
     * добавление объекта поискового индекса
     * @param searchIndex объект поискового индекса
     */
    public void addIndex(SearchIndex searchIndex)
    {
        searchIndexList.add(searchIndex);
    }

    /**
     * добавление объекта класса SearchResult в searchResultList
     * @param searchResult объект класса SearchResult
     */
    public static void addSearchResult(SearchResult searchResult)
    {
        searchResultList.add(searchResult);
    }

    /**
     * получение массива данных для ответа на поисковый запрос
     * @return массив данных для ответа на поисковый запрос
     */
    public static SearchResultData[] generateResultArray()
    {
        int arraySize = searchResultList.size();

        SearchResultData[] resultArray = new SearchResultData[arraySize];

        for (int i = 0; i < arraySize; i++)
        {
            resultArray[i] = new SearchResultData(searchResultList.get(i));
        }

        return resultArray;
    }

    /**
     *  очистка searchResultList и обнуление максимальной относительной релевантности
     */
    public static void clearResults()
    {
        searchResultList.clear();
        maxRelevance = 0.0f;
    }


    /**
     * очистка siteSearchResultMap
     */
    public static void clearSiteSearchResult()
    {
        siteSearchResultMap.clear();
    }

    /**
     * ограничение количества результатов поиска в соответствии с параметром поискового запроса
     * @param resultsQtyLimit
     */
    public static void limitResults(int resultsQtyLimit)
    {
        if(searchResultList.size() > resultsQtyLimit)
        {
            List<SearchResult> limitedList = searchResultList.subList(0, resultsQtyLimit);
            searchResultList = limitedList;
        }
    }

    /**
     * запуск расчёта и заполнения относительных релевантностей страниц для всех объектов SearchResult
     */
    private static void calculateRelRelevance()
    {
        searchResultList.stream().forEach(searchResult ->
                searchResult.setRelRelevance(searchResult.getAbsRelevance()/SearchResult.getMaxRelevance()));
    }

    /**
     * метод запускает расчёт и заполнение и относительных релевантностей страниц для всех объектов SearchResult
     * после расчёта осуществляется сортировка страниц по относительной релевантности в обратном порядке
     */
    public static void generateSortedTotalResults()
    {
        calculateRelRelevance();

        searchResultList.sort(Comparator.comparing((SearchResult searchResult) ->
                searchResult.getRelRelevance()).reversed());
    }

    /**
     * метод запускает расчёт и заполнение абсолютных релевантностей страниц для всех объектов SearchResult
     */
    public static void calculateAbsRelevanceForSitePages()
    {
        for(SearchResult curSearchResult : siteSearchResultMap.values())
        {
            float curAbsRelevance = 0f;

            List<SearchIndex> curSearchIndexList = curSearchResult.getSearchIndexList();

            for(SearchIndex curSearchIndex : curSearchIndexList)
            {
                curAbsRelevance += curSearchIndex.getRank();
            }

            curSearchResult.setAbsRelevance(curAbsRelevance);
            addSearchResult(curSearchResult);
            setMaxRelevance(Float.max(curAbsRelevance, SearchResult.getMaxRelevance()));
        }
    }

    /**
     * метод заполняет поле title, для всех объектов SearchResult из searchResultList
     */
    public static void setTitles()
    {

        for(SearchResult searchResult : searchResultList)
        {
            Document htmlDocument = searchResult.getHtmlDocument();
            List<String> titles = htmlDocument.select("title").eachText();
            String title = (titles.size() == 0) ? "" : titles.get(0);
            searchResult.setTitle(title);
        }
    }

    public static List<SearchResult> getSearchResultList()
    {
        return searchResultList;
    }


    public static Map<Integer, SearchResult> getSiteSearchResultMap()
    {
        return siteSearchResultMap;
    }


    public static float getMaxRelevance()
    {
        return maxRelevance;
    }

    public static void setMaxRelevance(float maxRelevance)
    {
        SearchResult.maxRelevance = maxRelevance;
    }

    public List<SearchIndex> getSearchIndexList()
    {
        return searchIndexList;
    }

    public int getPageId()
    {
        return pageId;
    }

    public float getAbsRelevance()
    {
        return absRelevance;
    }

    public void setAbsRelevance(float absRelevance)
    {
        this.absRelevance = absRelevance;
    }

    public float getRelRelevance()
    {
        return relRelevance;
    }

    public void setRelRelevance(float relRelevance)
    {
        this.relRelevance = relRelevance;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getSnippet()
    {
        return snippet;
    }

    public void setSnippet(String snippet)
    {
        this.snippet = snippet;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public Document getHtmlDocument()
    {
        return htmlDocument;
    }

    public void setHtmlDocument(Document htmlDocument)
    {
        this.htmlDocument = htmlDocument;
    }
}
