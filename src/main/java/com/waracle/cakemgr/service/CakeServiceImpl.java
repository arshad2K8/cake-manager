package com.waracle.cakemgr.service;

import com.waracle.cakemgr.CakeEntity;
import com.waracle.cakemgr.dao.CakeDao;
import com.waracle.cakemgr.dao.CakeDaoImpl;
import com.waracle.cakemgr.utils.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CakeServiceImpl implements CakeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CakeServiceImpl.class);
    private final CakeDao<CakeEntity, Integer> cakeDao;

    public CakeServiceImpl() {
        cakeDao = new CakeDaoImpl();
    }

    @Override
    public void persist(CakeEntity cakeEntity) {
        if(cakeEntity == null) {
            LOGGER.error("Eww trying to persist cakeEntity it cant be null");
            return;
        }
        this.cakeDao.persist(cakeEntity);
    }

    @Override
    public List<CakeEntity> findAll() {
        return this.cakeDao.findAll();
    }
}
