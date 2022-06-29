package main.controller.responses;

import main.service.site_map_and_index.implementation.Statistics;

/**
 * Класс ответа на запрос статистики по сайтам
 */
public class ResponseStatistics extends Response
{
    // Статистические данные
    private final Statistics statistics;

    public ResponseStatistics(boolean result, Statistics statistics)
    {
        super(result);
        this.statistics = statistics;
    }

    public Statistics getStatistics()
    {
        return statistics;
    }
}
