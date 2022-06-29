package main.repository.dao.standard_crud;

import main.repository.entity.Field;
import org.springframework.data.repository.CrudRepository;

/**
 * интерфейс, описывающий CRUD операции с полями страниц сайтов
 */

public interface FieldRepository extends CrudRepository<Field, Integer> {}