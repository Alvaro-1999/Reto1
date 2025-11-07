package gymapp.service;

import java.util.List;

public interface ServiceInterface<T> {

    void save(T t) throws Exception;

    T find(T t) throws Exception; // mantiene compatibilidad con tu código actual

    List<T> findAll() throws Exception;

    void update(T t) throws Exception; // corregido el nombre

    void delete(T t) throws Exception;
}
