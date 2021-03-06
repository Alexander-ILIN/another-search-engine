package main.service.db_service;

import main.repository.entity.Page;
import java.util.List;
import java.util.Optional;

/**
 * интерфейс, позволяющий запускать операции со страницами в БД
 */
public interface PageService
{
    /**
     * поиск страницы в БД по её id
     * @param id id страницы
     * @return Optional, содержащий страницу
     */
    Optional<Page> findById(int id);

    /**
     * получение количества страниц в БД
     * @return количество страниц в БД
     */
    long count();

    /**
     * проверка, какие страницы из перечня уже существуют в БД
     * @param pages перечень страниц
     * @param siteId id сайта
     * @return список страниц, существующих в БД и относящихся к сайту с указанным id
     */
    List<Page> findByPagesAndSiteId(Iterable<Page> pages, int siteId);

    /**
     * получение количества страниц, относящихся к сайту
     * @param siteId id сайта
     * @return количество страниц сайта с заданным id
     */
    long countAllBySiteId(int siteId);

    /**
     * сохранение перечня страниц в БД
     * @param pages перечень объектов Page
     * @return перечень объектов Page, сохранённых в БД
     */
    Iterable<Page> saveAll(Iterable<Page> pages);

    /**
     * поиск страниц по ссылке и id сайта
     * @param pageUrl ссылка на страницу
     * @param siteId id сайта
     * @return список страниц, имеющих указанный url и относящихся к сайту с указанным id
     */
    List<Page> findByUrlAndSiteId(String pageUrl, int siteId);

    /**
     * удаление всех страниц, относящихся к сайту с указанным id
     * @param siteId id сайта
     */
    void deleteBySiteId(int siteId);

    /**
     * поиск страниц по перечню ID
     * @param ids перечень ID страниц
     * @return перечень найденных страниц
     */
    Iterable<Page> findAllById(Iterable<Integer> ids);
}
