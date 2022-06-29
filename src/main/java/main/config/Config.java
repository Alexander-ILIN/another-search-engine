package main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Конфигурационный класс. Используется для доступа к параметрам из "application.yaml"
 */

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("variables")
public class Config
{
    // Префикс таблиц
    private String tablesPrefix;

    // Информация о сайтах: keys - url, name, values - значения
    private List<Map<String, String>> sites;

    // User agent
    private String userAgent;

    // Referrer
    private String referrer;

    // Размер буфера для сохранения страниц
    private int pageBufferSize;

    // Размер буфера для сохранения лемм
    private int lemmaBufferSize;

    // Размер буфера для сохранения индекса
    private int indexBufferSize;

    public List<Map<String, String>> getSites()
    {
        return sites;
    }

    public void setSites(List<Map<String, String>> sites)
    {
        this.sites = sites;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public int getPageBufferSize() {
        return pageBufferSize;
    }

    public void setPageBufferSize(int pageBufferSize) {
        this.pageBufferSize = pageBufferSize;
    }

    public int getLemmaBufferSize() {
        return lemmaBufferSize;
    }

    public void setLemmaBufferSize(int lemmaBufferSize) {
        this.lemmaBufferSize = lemmaBufferSize;
    }

    public int getIndexBufferSize() {
        return indexBufferSize;
    }

    public void setIndexBufferSize(int indexBufferSize) {
        this.indexBufferSize = indexBufferSize;
    }

    public String getTablesPrefix()
    {
        return tablesPrefix;
    }

    public void setTablesPrefix(String tablesPrefix)
    {
        this.tablesPrefix = tablesPrefix;
    }
}
