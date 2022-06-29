package main.service.db_service.implementation;

import main.repository.dao.standard_crud.SiteRepository;
import main.repository.entity.Site;
import main.service.db_service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * класс, реализующий запуск операций с сайтами в БД
 * для запуска операций используется интерфейс SiteRepository
 */
@Component
class SiteServiceImpl implements SiteService
{
    @Autowired
    SiteRepository siteRepository;

    @Override
    public Iterable<Site> saveAll(Iterable<Site> sites)
    {
        return siteRepository.saveAll(sites);
    }

    @Override
    public Iterable<Site> findAll()
    {
        return siteRepository.findAll();
    }

    @Override
    public void delete(Site site)
    {
        siteRepository.delete(site);
    }

    @Override
    public List<Site> findByUrl(String url)
    {
        return siteRepository.findByUrl(url);
    }

    @Override
    public long count()
    {
        return siteRepository.count();
    }

    @Override
    public Site save(Site site)
    {
        return siteRepository.save(site);
    }
}
