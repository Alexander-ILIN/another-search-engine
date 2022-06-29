package main.service.db_service.implementation;

import main.repository.entity.SearchIndex;
import main.repository.dao.non_standard_operations.SearchIndexNonStandardRepository;
import main.repository.dao.standard_crud.SearchIndexRepository;
import main.service.db_service.SearchIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * класс, реализующий запуск операций с индексами страниц в БД
 * для запуска операций используются интерфейсы SearchIndexRepository и SearchIndexNonStandardRepository
 */
@Component
class SearchIndexServiceImpl implements SearchIndexService
{
    @Autowired
    private SearchIndexRepository searchIndexRepository;

    @Autowired
    private SearchIndexNonStandardRepository searchIndexNonStandardRepository;

    @Override
    public List<SearchIndex> findByLemmaId(int lemmaId)
    {
        return  searchIndexRepository.findByLemmaId(lemmaId);
    }

    @Override
    public void saveIndexes(Map<String, Integer> lemmasMap, Map<String, Float> rankedPageLemmasMap, Integer pageId)
    {
        searchIndexNonStandardRepository.saveIndexes(lemmasMap, rankedPageLemmasMap, pageId);
    }

    @Override
    public List<SearchIndex> findByPageId(int pageId)
    {
        return searchIndexRepository.findByPageId(pageId);
    }

    @Override
    public void deleteByPageId(int pageId)
    {
        searchIndexNonStandardRepository.deleteByPageId(pageId);
    }


    @Override
    public void deleteBySiteId(int siteId)
    {
        searchIndexNonStandardRepository.deleteBySiteId(siteId);
    }
}
