package main.repository.entity;

import javax.persistence.*;

/**
 * Леммы, встречающиеся в текстах
 */

@Entity
@Table(name = "lemma", uniqueConstraints = {@UniqueConstraint(columnNames = {"lemma", "site_id"})},
        indexes = @Index(columnList = "lemma"))
public class Lemma
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "lemma", nullable = false, columnDefinition = "VARCHAR(255)")
    private String lemma; // нормальная форма слова

    @Column(name = "frequency", nullable = false)
    private Integer frequency; // количество страниц, на которых слово встречается хотя бы один раз

    @Column(name = "site_id", nullable = false)
    private int siteId;

    public Lemma()
    {
    }

    public Lemma(String lemma, Integer frequency)
    {
        this.lemma = lemma;
        this.frequency = frequency;
    }

    public Lemma(String lemma)
    {
        this.lemma = lemma;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
}
