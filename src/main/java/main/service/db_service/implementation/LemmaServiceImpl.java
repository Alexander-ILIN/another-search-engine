package main.service.db_service.implementation;

import main.repository.entity.Lemma;
import main.repository.dao.non_standard_operations.LemmaNonStandardRepository;
import main.repository.dao.standard_crud.LemmaRepository;
import main.service.db_service.LemmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * класс, реализующий запуск операций с леммами в БД
 * для запуска операций используются интерфейсы LemmaRepository и LemmaNonStandardRepository
 */
@Component
class LemmaServiceImpl implements LemmaService
{
    @Autowired
    private LemmaNonStandardRepository lemmaNonStandardRepository;

    @Autowired
    LemmaRepository lemmaRepository;

    @Override
    public Map<String, Integer> getLemmasByStrings(Collection<String> lemmaStrings, int siteId)
    {
        return lemmaNonStandardRepository.getLemmasByStrings(lemmaStrings, siteId);
    }

    @Override
    public List<Lemma> findLemmas(Collection<String> lemmaStrings, int siteId)
    {
        return lemmaNonStandardRepository.findLemmas(lemmaStrings, siteId);
    }

    @Override
    public long count()
    {
        return lemmaRepository.count();
    }

    @Override
    public long countAllBySiteId(int siteId)
    {
        return lemmaRepository.countAllBySiteId(siteId);
    }


    @Override
    public Iterable<Lemma> findAllById(Iterable<Integer> ids)
    {
        return lemmaRepository.findAllById(ids);
    }

    @Override
    public void delete(Lemma lemma)
    {
        lemmaRepository.delete(lemma);
    }

    @Override
    public Iterable<Lemma> saveAll(Iterable<Lemma> lemmas)
    {
        return lemmaRepository.saveAll(lemmas);
    }

    @Override
    public void deleteBySiteId(int siteId)
    {
        lemmaNonStandardRepository.deleteBySiteId(siteId);
    }
}
