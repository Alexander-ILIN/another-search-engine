package main.service.site_map_and_index.implementation;

import main.repository.entity.Site;
import main.service.site_map_and_index.interfaces.SiteMappingProcessor;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * класс, описывающий сайт
 */
class AuxSiteData
{
    private final String rootUrl;       // ссылка на сайт
    private final int siteId;           // ID сайта в таблице site
    private Set<String> checkedUrls;    // сет ссылок на страницы сайта, пройденные системой обхода страниц
    private final int rootUrlLen;       // длина ссылки на сайт
    private final SiteMappingProcessor siteMapper; // объект, используемый для сохранения страниц и запуска их индексации
    private final String userAgent; // user agent
    private final String referrer; // referrer
    private volatile boolean terminated = false; // статус прерывания процесса индексации

    /**
     * конструктор класса
     * @param site - сайт, для которого необходимо получить карту
     */
    public AuxSiteData(Site site, SiteMappingProcessor siteMapper, String userAgent, String referrer)
    {
        String tempRootUrl = site.getUrl();
        this.siteId = site.getId();
        this.siteMapper = siteMapper;
        this.userAgent = userAgent;
        this.referrer = referrer;

        if (tempRootUrl.endsWith("/"))
        {
            int length = tempRootUrl.length();
            this.rootUrl = tempRootUrl.substring(0, length - 1);
        }
        else
        {
            this.rootUrl = tempRootUrl;
        }

        this.rootUrlLen = rootUrl.length();

        checkedUrls = new ConcurrentSkipListSet<>();
    }

    /**
     * проверка, была ли страница уже пройдена системой обхода страниц сайта
     * @param url - ссылка на страницу
     * @return true, если страница была пройдена, false в противном случае
     */
    public boolean isUrlChecked(String url)
    {
        return checkedUrls.contains(url);
    }

    /**
     * добавление страницы в сет пройденных страниц
     * проверка наличия ссылки с "/" и без "/"
     * @param url - ссылка на страницу
     */
    public synchronized void addCheckedUrl(String url)
    {
        String duplicatedUrl;

        if(url.endsWith("/"))
        {
            int length = url.length();
            duplicatedUrl = url.substring(0, length - 1);
        }
        else
        {
            duplicatedUrl = url;
        }

        if(!checkedUrls.contains(duplicatedUrl))
        {
            checkedUrls.add(url);
        }
    }

    /**
     * получение количества страниц, пройденных системой обхода страниц
     * @return
     */
    public int getCheckedUrlsQty()
    {
        return checkedUrls.size();
    }

    public String getRootUrl()
    {
        return rootUrl;
    }

    public int getSiteId()
    {
        return siteId;
    }

    public int getRootUrlLen()
    {
        return rootUrlLen;
    }

    public SiteMappingProcessor getSiteMapper() {
        return siteMapper;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void terminate()
    {
        terminated = true;
    }

    public boolean isTerminated()
    {
        return terminated;
    }
}
