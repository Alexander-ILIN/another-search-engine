package main.repository.dao.standard_crud;

import main.repository.entity.Page;
import org.springframework.data.repository.CrudRepository;

/**
 * интерфейс, описывающий CRUD операции с индексированными страницами
 */

public interface PageRepository extends CrudRepository<Page, Integer>
{
    long countAllBySiteId(int siteId);
}