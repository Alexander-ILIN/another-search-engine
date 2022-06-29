package main.service.search_system.implementation;

/**
 * класс, используемый для хранения данных для ответа на поисковый запрос
 */
public class SearchResultData
{
    private final String site;      // ссылка на сайт, к которому относится страница
    private final String siteName;  // имя сайта, к которому относится страница
    private final String uri;       // ссылка на страницу
    private final String title;     // заголовок страницы
    private final String snippet;   // фрагмент текста, в котором найдены совпадения
    private final float relevance;  // относительная релевантность страницы

    /**
     * конструктор класса
     * @param searchResult объект класса SearchResult
     */
    public SearchResultData(SearchResult searchResult)
    {
        this.site = searchResult.getSiteUrl();
        this.siteName = searchResult.getSiteName();
        this.uri = searchResult.getPageUrl();
        this.title = searchResult.getTitle();
        this.snippet = searchResult.getSnippet();
        this.relevance = searchResult.getRelRelevance();
    }

    public String getSite()
    {
        return site;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public String getUri()
    {
        return uri;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSnippet()
    {
        return snippet;
    }

    public float getRelevance()
    {
        return relevance;
    }
}