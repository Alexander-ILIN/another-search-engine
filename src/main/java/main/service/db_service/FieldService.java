package main.service.db_service;

import main.repository.entity.Field;

/**
 * интерфейс, позволяющий запускать операции с полями страниц сайтов в БД
 */
public interface FieldService
{
    /**
     * сохранение перечня полей HTML документа в БД
     * @param fields перечень объектов Field
     * @return перечень объектов Field, сохранённых в БД
     */
    Iterable<Field> saveAll(Iterable<Field> fields);

    /**
     * нахождение всех полей HTML документа, содержащихся в БД
     * @return перечень объектов Field, содержащихся в БД
     */
    Iterable<Field> findAll();
}
