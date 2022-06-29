package main.service.site_map_and_index.interfaces;

import main.controller.responses.Response;

/**
 * Интерфейс, использующийся для сбора статистики по индексации сайтов
 */
public interface StatisticsProcessor
{
    /**
     * Сбор статистики индексации: общей и по каждому сайту
     * @return объект Response, содержащий в себе объект StatisticsProcessor
     */
    Response getStatistics();
}
