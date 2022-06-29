package main.repository.dao.standard_crud;

import main.repository.entity.Lemma;
import org.springframework.data.repository.CrudRepository;

/**
 * интерфейс, описывающий CRUD операции с леммами
 */

public interface LemmaRepository extends CrudRepository<Lemma, Integer>
{
    long countAllBySiteId(int siteId);
}