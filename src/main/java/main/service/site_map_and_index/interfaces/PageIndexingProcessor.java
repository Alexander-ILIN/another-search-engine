package main.service.site_map_and_index.interfaces;

import main.repository.entity.Page;

/**
 * интерфейс, использующийся для индексации страниц и сохранения информации о леммах и индксах
 */
public interface PageIndexingProcessor
{
    /**
     * запуск индексации страницы
     * @param page - экземпляр класса Page, который необходимо проиндексировать
     */
    void indexPage(Page page);

    /**
     * удаление / обновление информации при обновлении отдельной страницы
     * @param page страница
     */
    void deletePageIndexData(Page page);

    /**
     * удаление из БД данных, относящихся к сайту
     * @param siteId id сайта, данные о котором необходимо удалить
     */
    void removeSiteIndexData(int siteId);
}
