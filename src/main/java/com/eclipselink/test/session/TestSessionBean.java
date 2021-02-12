package com.eclipselink.test.session;

import com.eclipselink.test.bean.TestEntity;
import com.eclipselink.test.dao.TestDao;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class TestSessionBean {
    private static final Logger LOGGER = Logger.getLogger(TestSessionBean.class.getSimpleName());

    @PersistenceContext(unitName = "test2-PU")
    private EntityManager entityManager;

    @Inject
    private TestDao dao;

    @Transactional
    public List<String> requestAll(){
        request1();
        return requestNativeNamedQuery();
    }

    @Transactional
    public void request1(){

        LOGGER.info("***** start request 1");

        List<TestEntity> list = entityManager
                .createNamedQuery("nativeSqlQuery", TestEntity.class)
                .getResultList();

        LOGGER.info("***** end request 1");
   }

    @Transactional
    public List<String> requestNativeNamedQuery(){

          LOGGER.info("***** start request Native Named Query");

        List<String> list = dao.getNativeNamedQuery();

        LOGGER.info("***** end request Native Named Query");

        return list;
    }

    @Transactional
    public List<String> requestNativeNamedQueryDirect(){

        LOGGER.info("***** start request Named Query direct");

        List<TestEntity> list = entityManager
                .createNamedQuery("nativeSqlQuery", TestEntity.class)
                .getResultList();

        LOGGER.info("***** end request Named Query direct");


        return list.stream().map(t -> t.getNaam()).collect(Collectors.toList());
    }

    @Transactional
    public List<String> requestNativeQuery(){

        LOGGER.info("***** start request native query");

        List<String> list = dao.getNativeQuery();

        LOGGER.info("***** end request native query");

        return list;
    }

    @Transactional
    public List<String> requestJpaNamedQuery(){

        LOGGER.info("***** start request JPA Named query");

        List<String> list = dao.getJpaNamedQuery();

        LOGGER.info("***** end request JPA Named query");

        return list;
    }
}
