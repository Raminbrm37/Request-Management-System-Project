package ir.maktab.finalproject.service;

import ir.maktab.finalproject.model.BaseEntity;
import ir.maktab.finalproject.util.exception.SubjectAlreadyExistException;

import java.util.List;
import java.util.Set;

public interface BaseEntityService<E extends BaseEntity<Long>, T> {
    void save(E entity) ;
    E findById(T id);
    List<E> findAll();
    void remove(E entity);
    void removeById(T id);


}
