package com.eclipselink.test.dao;

import com.eclipselink.test.bean.TestEntity;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless  // Must be an EJB bean.
public class TestDao {
	private static final Logger LOGGER = Logger.getLogger(TestDao.class.getSimpleName());

	@PersistenceContext(unitName = "test2-PU")
	private EntityManager entityManager;

	public List<String> getJpaNamedQuery(){

		TypedQuery<TestEntity> query=entityManager.createNamedQuery("jpaQuery", TestEntity.class);

		List<TestEntity> list = query.getResultList();

		return list.stream().map(t -> t.getNaam()).collect(Collectors.toList());
	}

	public List<String> getNativeNamedQuery(){

		TypedQuery<TestEntity> query=entityManager.createNamedQuery("nativeSqlQuery", TestEntity.class);

		List<TestEntity> list = query.getResultList();

		return list.stream().map(t -> t.getNaam()).collect(Collectors.toList());

	}

	public List<String> getNativeQuery(){

		List list=entityManager.createNativeQuery("select r.* from test_table r", TestEntity.class).getResultList();


		return Collections.emptyList();

	}

}
