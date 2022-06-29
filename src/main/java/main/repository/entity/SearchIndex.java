package main.repository.entity;

import javax.persistence.*;

/**
 * Поисковый индекс
 */

@Entity
@Table(name = "index")
public class SearchIndex
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "page_id", nullable = false)
    private int pageId; // Идентификатор страницы

    @Column(name = "lemma_id", nullable = false)
    private int lemmaId; // Идентификатор леммы

    @Column(name = "lemma_rank", nullable = false)
    private float rank; // Ранг леммы на странице

    public SearchIndex()
    {
    }

    public SearchIndex(int pageId, int lemmaId, float rank)
    {
        this.pageId = pageId;
        this.lemmaId = lemmaId;
        this.rank = rank;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPageId()
    {
        return pageId;
    }

    public void setPageId(int pageId)
    {
        this.pageId = pageId;
    }

    public int getLemmaId()
    {
        return lemmaId;
    }

    public void setLemmaId(int lemmaId)
    {
        this.lemmaId = lemmaId;
    }

    public float getRank()
    {
        return rank;
    }

    public void setRank(float rank)
    {
        this.rank = rank;
    }
}
