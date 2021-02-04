package com.waracle.cakemgr.dao;

import com.waracle.cakemgr.CakeEntity;
import com.waracle.cakemgr.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class CakeDaoImpl implements CakeDao<CakeEntity, Integer> {

    private final static Logger LOGGER = LoggerFactory.getLogger(CakeDaoImpl.class);

    private SessionFactory sessionFactory;

    public CakeDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void persist(CakeEntity cakeEntity) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(cakeEntity);
            LOGGER.info("adding cake entity");
            transaction.commit();
        } catch (ConstraintViolationException ex) {
            LOGGER.error("cakeEntity insert failed {}", cakeEntity);
            LOGGER.error("ConstraintViolationException ", ex);
            if (transaction!=null) transaction.rollback();
        } catch (Exception e) {
            if (transaction!=null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CakeEntity> findAll() {
        Session session = getSessionFactory().openSession();
        List<CakeEntity> cakes;
        try {
            cakes = (List<CakeEntity>) session.createCriteria(CakeEntity.class).list();
        } finally {
            session.close();
        }
        return cakes == null ? Collections.emptyList() : cakes;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
