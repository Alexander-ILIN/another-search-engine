package main.repository.entity;

import javax.persistence.*;

/**
 * Проиндексированная страница
 */

@Entity
@Table(name = "page", uniqueConstraints = {@UniqueConstraint(columnNames = {"page_url", "site_id"})}, indexes = @Index(columnList = "page_url"))
public class Page implements Comparable<Page>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "page_url", nullable = false, columnDefinition = "VARCHAR(255)")
    private String pageUrl; // ссылка на текущую страницу

    @Column(name = "response_code" ,nullable = false)
    private int responseCode; // код ответа

    @Column(name = "page_content", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String pageContent; // контент страницы

    @Column(name = "site_id", nullable = false)
    private int siteId;

    public Page()
    {
    }

    public Page(String pageUrl, int responseCode, String pageContent, int siteId)
    {
        this.pageUrl = pageUrl;
        this.responseCode = responseCode;
        this.pageContent = pageContent;
        this.siteId = siteId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Override
    public int compareTo(Page page) {
        return (this.pageUrl).compareTo(page.pageUrl);
    }
}