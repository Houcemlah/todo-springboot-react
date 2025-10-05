package com.example.demo.services;

import com.example.demo.repositories.GenericRepository;
import java.util.List;
import java.util.Optional;

public abstract class GenericServices<T, ID> {

    protected abstract GenericRepository<T, ID> getRepository();

    public List<T> findAll(){
        return getRepository().findAll();
    }

    public Optional<T> findByID(ID id){
        return getRepository().findById(id);
    }

    public T create(T entity){
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }
}
