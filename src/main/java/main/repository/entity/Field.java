package main.repository.entity;

import javax.persistence.*;

/**
 * Поля на страницах сайтов
 */

@Entity
@Table(name = "field")
public class Field
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private int id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name; // Имя поля

    @Column(name = "selector", nullable = false, columnDefinition = "VARCHAR(255)")
    private String selector; // CSS-выражение

    @Column(name = "weight", nullable = false, columnDefinition = "FLOAT")
    private float weight; // Релевантность

    public Field()
    {
    }

    public Field(String name, String selector, float weight)
    {
        this.name = name;
        this.selector = selector;
        this.weight = weight;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSelector()
    {
        return selector;
    }

    public void setSelector(String selector)
    {
        this.selector = selector;
    }

    public float getWeight()
    {
        return weight;
    }

    public void setWeight(float weight)
    {
        this.weight = weight;
    }
}
