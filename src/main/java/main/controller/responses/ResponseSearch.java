package main.controller.responses;

import main.service.search_system.implementation.SearchResultData;

/**
 * Класс ответа на поисковый запрос
 */
public class ResponseSearch extends Response
{
    // Количество результатов
    private final int count;

    // Данные ответа
    private final SearchResultData[] data;

    public ResponseSearch(boolean result, int count, SearchResultData[] data)
    {
        super(result);
        this.count = count;
        this.data = data;
    }

    public int getCount()
    {
        return count;
    }

    public SearchResultData[] getData()
    {
        return data;
    }
}
