package com.sim.tunisierando.Services.Interfaces;

/**
 * Created by Aymen on 15/11/2017.
 */

public interface IService<T,I> {
    void Add(T t);
    void Update(T t);
    void Delete(T t);
    void getAll(VolleyCallback callback);
    T GetById(I i);
}
