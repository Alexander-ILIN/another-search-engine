package main.service.search_system.implementation;

import main.service.lemmas.Morphology;
import main.repository.entity.*;
import main.service.db_service.LemmaService;
import main.service.db_service.PageService;
import main.service.db_service.SearchIndexService;
import main.service.db_service.SiteService;
import main.service.search_system.SearchResultProcessor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * класс, реализующий выполнение поискового запроса пользователя
 */
@Component
class SearchResultGenerator implements SearchResultProcessor
{
    @Autowired
    private Morphology morphology; // лемматизатор

    @Autowired
    private LemmaService lemmaService; // операции с леммами в БД

    @Autowired
    private SearchIndexService searchIndexService; // операции с индексами страниц в БД

    @Autowired
    private PageService pageService; // операции со страницами в БД

    @Autowired
    SiteService siteService; // операции с сайтами в БД

    private boolean isMapInitialized = false;

    // символы, обрабатываемые на страницах и в запросах
    private static final String ALLOWED_SYMBOLS_REGEX = "[а-яА-Яa-zA-Z0-9]+";

    // разбиение текста на слова, пробелы и остальные знаки
    private static final String REQ_SPLIT_REGEX = "(?<=[^а-яА-Яa-zA-Z0-9])|(?=[^а-яА-Яa-zA-Z0-9])";

    // открывающий тег
    private static final String OP_TAG = "<b>";

    // закрывающий тег
    private static final String CL_TAG = "</b>";

    // количество слов, пробелов и прочих остальных знаков перед первым и после последнего искомого слова в snippet
    private static final int LEADING_AND_TRAILING_ITEMS_QTY = 60;

    /**
     * пошагово создаются и заполняются объекты SearchResult
     * @param queryText строка, содержащая поисковый запрос
     * @param siteUrl ссылка на сайт, по которому необходимо выполнить поиск
     * @param resultsQtyLimit количество результатов, которое необходимо вывести
     * @return массив объектов SearchResultData, который впоследствии используется для вывода результата поиска пользователю
     * @throws IOException исключение, если отсутствуют проиндексированные сайты
     */
    @Override
    public SearchResultData[] getSearchResults(String queryText, String siteUrl, int resultsQtyLimit) throws IOException
    {
        SearchResult.clearResults();

        Set<Site> siteSet;

        if(siteUrl == null)
        {
            siteSet = getValidSites();
        }
        else
        {
            Site singleSite = getSite(siteUrl);

            siteSet = new HashSet<>();
            siteSet.add(singleSite);
        }

        Set<String> lemmasSet = morphology.getTextLemmas(queryText);

        for (Site curSite : siteSet)
        {
            getSiteSearchResults(lemmasSet, curSite);
        }

        SearchResult.generateSortedTotalResults();

        SearchResult.limitResults(resultsQtyLimit);

        addPagesData();

        SearchResult.setTitles();

        fillInSnippets(lemmasSet);

        SearchResultData[] resultData = SearchResult.generateResultArray();

        return resultData;
    }

    /**
     * запуск поиска страниц, содержащих леммы из поискового запроса пользователя
     * @param lemmasSet сет лемм из поискового запроса пользователя
     * @param site сайт, по которому осуществляется поиск
     */
    private void getSiteSearchResults(Set<String> lemmasSet, Site site)
    {
        isMapInitialized = false;
        SearchResult.clearSiteSearchResult();

        List<Lemma> lemmas = lemmaService.findLemmas(lemmasSet, site.getId());

        for (Lemma curLemma : lemmas)
        {
            List<SearchIndex> curSearchIndexes = searchIndexService.findByLemmaId(curLemma.getId());

            if(!isMapInitialized)
            {
                try
                {
                    initializeSearchResult(site, curSearchIndexes);
                }
                catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            else
            {
                reduceSearchResult(curSearchIndexes);
            }
        }
        SearchResult.calculateAbsRelevanceForSitePages();
    }

    /**
     * метод создаёт объекты SearchResult для самой редкой леммы и устанавливает перемнную isMapInitialized = true
     * выполняется один раз для поискового запроса для каждого сайта, если перемнная isMapInitialized = false
     * @param searchIndexes список объектов SearchIndex, относящийся к заданной лемме
     */
    private void initializeSearchResult(Site site, List<SearchIndex> searchIndexes)
    {
        for(SearchIndex curSearchIndex : searchIndexes)
        {
            int curPageId = curSearchIndex.getPageId();

            SearchResult curSearchResult = new SearchResult(site, curPageId, curSearchIndex);
            SearchResult.getSiteSearchResultMap().put(curPageId, curSearchResult);
        }

        isMapInitialized = true;
    }

    /**
     * метод исключает страницы из результатов поиска страницы, которые не содержат заданной леммы
     * выполняется для каждой леммы из поискового запроса, кроме первой (самой редкой), при isMapInitialized = true
     * @param searchIndexes список объектов SearchIndex, относящийся к заданной лемме
     */
    private void reduceSearchResult(List<SearchIndex> searchIndexes)
    {
        Map<Integer, SearchIndex> curIndexMap = new HashMap<>();

        for(SearchIndex curSearchIndex : searchIndexes)
        {
            curIndexMap.put(curSearchIndex.getPageId(), curSearchIndex);
        }

        Set<Integer> toLeave = new HashSet<>(SearchResult.getSiteSearchResultMap().keySet());
        toLeave.retainAll(curIndexMap.keySet());

        for(int pageId : SearchResult.getSiteSearchResultMap().keySet())
        {
            if(toLeave.contains(pageId))
            {
                SearchResult curSearchResult = SearchResult.getSiteSearchResultMap().get(pageId);
                curSearchResult.addIndex(curIndexMap.get(pageId));
            }
        }
        SearchResult.getSiteSearchResultMap().keySet().removeIf(key -> !toLeave.contains(key));
    }

    /**
     * метод заполняет поля snippet для всех объектов SearchResult
     * @param reqLemmas сет лемм из запроса
     */
    private void fillInSnippets(Set<String> reqLemmas)
    {
        for(SearchResult curSearchResult : SearchResult.getSearchResultList())
        {
            setSnippet(reqLemmas, curSearchResult);
        }
    }

    /**
     * метод заполняет поле snippet для заданного объекта SearchResult
     * @param reqLemmas сет лемм, содержащихся в поисковом запросе
     * @param searchResult заданный объект SearchResult
     */
    private void setSnippet(Set<String> reqLemmas, SearchResult searchResult)
    {
        List<String> snippetWords = new LinkedList<>();
        int firstReqWordPos = 0;
        int lastReqWordPos = 0;

        Document htmlDocument = searchResult.getHtmlDocument();

        List<String> bodies = htmlDocument.select("body").eachText();

        if(bodies.size() == 0)
        {
            return;
        }

        for(String body : bodies)
        {
            String[] words = body.split(REQ_SPLIT_REGEX);

            for(String curWord : words)
            {
                if(isWordSearched(curWord, reqLemmas))
                {
                    snippetWords.add(OP_TAG);

                    if(firstReqWordPos == 0)
                    {
                        firstReqWordPos = snippetWords.size() - 1;
                    }

                    snippetWords.add(curWord);
                    snippetWords.add(CL_TAG);
                    lastReqWordPos = snippetWords.size() - 1;
                }
                else
                {
                    snippetWords.add(curWord);
                }
            }
        }

        String snippetText = generateSnippetText(snippetWords, firstReqWordPos ,lastReqWordPos);
        searchResult.setSnippet(snippetText);
    }

    /**
     * метод проверяет содержатся ли леммы заданного слова в поисковом запросе
     * @param word заданное слово
     * @param reqLemmas сет, содержащий леммы поискового запроса
     * @return true, если леммы заданного слова содержатся в поисковом запросе; false, если нет
     */
    private boolean isWordSearched (String word, Set<String> reqLemmas)
    {

        List<String> wordLemmas = morphology.getWordLemmas(word);

        if(wordLemmas.size() == 0)
        {
            if(!word.matches(ALLOWED_SYMBOLS_REGEX))
            {
                return false;
            }
            else
            {
                wordLemmas.add(word);
            }
        }

        for(String curLemma : wordLemmas)
        {
            if(reqLemmas.contains(curLemma))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * метод создаёт snippet
     * @param snippetWords список, содержащий текст страницы, разбитый на слова, пробелы и знаки препинания
     * @param firstReqWordPos индекс первого вхождения слова из поискового запроса в списке snippetWords
     * @param lastReqWordPos индекс последнего вхождения слова из поискового запроса в списке snippetWords
     * @return текст snippet
     */
    private String generateSnippetText(List<String> snippetWords, int firstReqWordPos, int lastReqWordPos)
    {
        int i = firstReqWordPos;
        int counter = 0;
        while(i > 0 && !snippetWords.get(i).equals(".") && counter < LEADING_AND_TRAILING_ITEMS_QTY)
        {
            --i;
            ++counter;
        }

        firstReqWordPos = i;

        if(snippetWords.get(firstReqWordPos).equals("."))
        {
            ++firstReqWordPos;
        }

        i = lastReqWordPos;
        counter = 0;
        while(i < snippetWords.size() - 1 && !snippetWords.get(i).equals(".") && counter < LEADING_AND_TRAILING_ITEMS_QTY)
        {
            ++i;
            ++counter;
        }
        lastReqWordPos = i;

        StringBuilder snippetText = new StringBuilder();
        for(int j = firstReqWordPos; j <= lastReqWordPos; j++)
        {
            snippetText.append(snippetWords.get(j));
        }
        return snippetText.toString().trim();
    }

    /**
     * получение сайта из запроса пользователя
     * @param siteUrl ссылка на сайт
     * @return null, сайт не выбран пользователем; объект класса Site, соответствующий выбору пользователя
     * @throws IOException исключение, если выбранный сайт не проиндексирован
     */
    private Site getSite(String siteUrl) throws IOException
    {
        Site result;
        if(siteUrl == null)
        {
            result = null;
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
            List<Site> sites = siteService.findByUrl(urlForSearch);

            if(sites.size() != 1)
            {
                result = null;
            }
            else
            {
                result = sites.get(0);

                if(!result.getStatus().equals(SiteStatus.INDEXED))
                {
                    throw new IOException("Выбранный сайт не проиндексирован");
                }
            }
        }
        return result;
    }

    /**
     * получение сета проиндексированных сайтов
     * @return сет, содержащий только проиндексированные сайты
     * @throws IOException исключение, если отсутствуют проиндексированные сайты
     */
    private Set<Site> getValidSites() throws IOException
    {
        Set<Site> sitesSet = new HashSet<>();
        Iterable<Site> sites = siteService.findAll();

        sites.forEach(sitesSet::add);

        if(sitesSet.isEmpty())
        {
            throw new IOException("Список сайтов пуст");
        }

        sitesSet.removeIf(site -> !site.getStatus().equals(SiteStatus.INDEXED));

        if(sitesSet.isEmpty())
        {
            throw new IOException("Отсутствуют проиндексированные сайты");
        }

        return sitesSet;
    }

    /**
     * получение объетов Page для добавления данных к объектам SearchResult после ограничения количества результатов поиска
     * @param searchResults - список объектов SearchResult
     * @return Map : Key = id страницы, value = объект Page (страница)
     */
    private Map<Integer, Page> getPagesBySearchResults(List<SearchResult> searchResults)
    {
        Map<Integer, Page> foundPagesMap = new HashMap<>();

        Set<Integer> pageIds = searchResults.stream().map(searchResult -> searchResult.getPageId()).collect(Collectors.toSet());
        Iterable<Page> foundPages = pageService.findAllById(pageIds);

        for(Page curPage: foundPages)
        {
            foundPagesMap.put(curPage.getId(), curPage);
        }

        return foundPagesMap;
    }

    /**
     * добавление данных к объектам SearchResult после ограничения количества результатов поиска
     * добавляемые данные:
     *      - HTML документ страницы;
     *      - ссылка на страницу
     */
    private void addPagesData()
    {
        List<SearchResult> searchResultList = SearchResult.getSearchResultList();

        Map<Integer, Page> pagesMap = getPagesBySearchResults(searchResultList);

        for(SearchResult curSearchResult : searchResultList)
        {
            Page curPage = pagesMap.get(curSearchResult.getPageId());
            if(null != curPage)
            {
                Document htmlDocument = Jsoup.parse(curPage.getPageContent());
                curSearchResult.setHtmlDocument(htmlDocument);
                curSearchResult.setPageUrl(curPage.getPageUrl());
            }
        }
    }
}