package main.service.db_service.implementation;

import main.repository.entity.Page;
import main.repository.dao.non_standard_operations.PageNonStandardRepository;
import main.repository.dao.standard_crud.PageRepository;
import main.service.db_service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * класс, реализующий запуск операций со страницами в БД
 * для запуска операций используются интерфейсы PageRepository и PageNonStandardRepository
 */
@Component
class PageServiceImpl implements PageService
{
    @Autowired
    PageRepository pageRepository;

    @Autowired
    PageNonStandardRepository pageNonStandardRepository;

    @Override
    public Iterable<Page> saveAll(Iterable<Page> pages)
    {
        return pageRepository.saveAll(pages);
    }

    @Override
    public Optional<Page> findById(int id)
    {
        return pageRepository.findById(id);
    }

    @Override
    public long count()
    {
        return pageRepository.count();
    }

    @Override
    public List<Page> findByPagesAndSiteId(Iterable<Page> pages, int siteId)
    {
        return pageNonStandardRepository.findByPagesAndSiteId(pages, siteId);
    }

    @Override
    public long countAllBySiteId(int siteId)
    {
        return pageRepository.countAllBySiteId(siteId);

    }

    @Override
    public List<Page> findByUrlAndSiteId(String pageUrl, int siteId)
    {
        return pageNonStandardRepository.findByUrlAndSiteId(pageUrl, siteId);
    }

    @Override
    public void deleteBySiteId(int siteId)
    {
        pageNonStandardRepository.deleteBySiteId(siteId);
    }

    @Override
    public Iterable<Page> findAllById(Iterable<Integer> ids)
    {
        return pageRepository.findAllById(ids);
    }
}
