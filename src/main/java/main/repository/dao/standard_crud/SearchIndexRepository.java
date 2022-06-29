package main.repository.dao.standard_crud;

import main.repository.entity.SearchIndex;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

/**
 * интерфейс, описывающий CRUD операции с поисковым индексом
 */

public interface SearchIndexRepository extends CrudRepository<SearchIndex, Integer>
{
    List<SearchIndex> findByLemmaId(int lemmaId);

    List<SearchIndex> findByPageId(int pageId);
}