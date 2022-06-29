package main.repository.dao.standard_crud;

import main.repository.entity.Site;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * интерфейс, описывающий CRUD операции с сайтами
 */
public interface SiteRepository extends CrudRepository<Site, Integer>
{
    List<Site> findByUrl(String url);
}
