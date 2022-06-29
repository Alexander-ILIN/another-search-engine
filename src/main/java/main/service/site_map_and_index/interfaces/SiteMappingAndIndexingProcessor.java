package main.service.site_map_and_index.interfaces;

import main.repository.entity.Site;


/**
 * интерфейс, используемый для получения карты сайта и запуска индексации
 */
public interface SiteMappingAndIndexingProcessor
{
    /**
     * запуск процесса получения всех страниц сайта и их индексации
     */
    void getAndIndexPages(Site site);

    /**
     * остановка индексации
     */
    void terminate();

    /**
     * запуск индексации отдельной страницы
     * @param url ссылка на страницу
     * @return статус выполения
     */
    int indexSinglePage(String url);

    /**
     * удаление данных, относящихся к сайту
     * @param site сайт, данные о котором необходимо удалить
     */
    void removeSiteData(Site site);
}
