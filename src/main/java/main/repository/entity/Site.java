package main.repository.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "site")
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false ,columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    private SiteStatus status;

    @Column(name = "status_time", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url", nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    public Site(SiteStatus status, LocalDateTime statusTime, String lastError, String url, String name)
    {
        this.status = status;
        this.statusTime = statusTime;
        this.lastError = lastError;
        this.url = url;
        this.name = name;
    }

    public Site()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public SiteStatus getStatus()
    {
        return status;
    }

    public void setStatus(SiteStatus status)
    {
        this.status = status;
    }

    public LocalDateTime getStatusTime()
    {
        return statusTime;
    }

    public void setStatusTime(LocalDateTime statusTime)
    {
        this.statusTime = statusTime;
    }

    public String getLastError()
    {
        return lastError;
    }

    public void setLastError(String lastError)
    {
        this.lastError = lastError;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
