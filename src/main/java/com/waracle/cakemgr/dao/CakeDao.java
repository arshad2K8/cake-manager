package com.waracle.cakemgr.dao;

import java.io.Serializable;
import java.util.List;

public interface CakeDao<T, Id extends Serializable> {
    void persist(T t);
    List<T> findAll();
}
