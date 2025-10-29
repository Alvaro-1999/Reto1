package com.gymapp.service;

import java.util.List;

public interface ServiceInterface<T> {
    void save(T entity) throws Exception;
    T find(T entity) throws Exception;
    List<T> findAll() throws Exception;
    void update(T entity) throws Exception;
    void delete(T entity) throws Exception;
}
