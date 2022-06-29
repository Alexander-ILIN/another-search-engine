package main.service.db_service.implementation;

import main.repository.dao.standard_crud.FieldRepository;
import main.repository.entity.Field;
import main.service.db_service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * класс, реализующий запуск операций с полями страниц сайтов в БД
 * для запуска операций используется интерфейс FieldRepository
 */
@Component
class FieldServiceImpl implements FieldService
{
    @Autowired
    FieldRepository fieldRepository;

    @Override
    public Iterable<Field> saveAll(Iterable<Field> fields)
    {
        return fieldRepository.saveAll(fields);
    }

    @Override
    public Iterable<Field> findAll()
    {
        return fieldRepository.findAll();
    }
}
