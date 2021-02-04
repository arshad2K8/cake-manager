package com.waracle.cakemgr.service;

import com.waracle.cakemgr.CakeEntity;

import java.util.List;

public interface CakeService {
    void persist(CakeEntity cakeEntity);
    List<CakeEntity> findAll();
}

