package com.database.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.database.bean.BaseBean;
import com.database.dao.BaseDao;

public abstract class BaseDaoImpl<B extends BaseBean> implements BaseDao<B> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private Class<B> queryClass;

	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		logger.debug("Set Autowired SessionFactory: {}",sessionFactory);
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @param queryClass
	 */
	public BaseDaoImpl(Class<B> queryClass) {
		super();
		this.queryClass = queryClass;
	}

	@Override
	public B getById(Integer id) {
		Session session = sessionFactory.openSession();
		B b = (B) session.get(this.getQueryClass(), id);
		session.close();
		return b;
	}

	@Override
	public List<B> getAll() {
		logger.info("get all");
		Session session = sessionFactory.openSession();
		List<B> beans = session.createQuery(
				"from " + this.getQueryClass().getName()).list();
		session.close();
		return beans;
	}

	@Override
	public int create(BaseBean b) {
		logger.debug("Save Basebean: {}",b);
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(b);
		tx.commit();
		session.close();
		return 0;
	}

	@Override
	public int update(BaseBean b) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(b);
		tx.commit();
		session.close();

		return 0;
	}

	@Override
	public int delete(BaseBean b) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete(b);
		tx.commit();
		session.close();

		return 0;
	}

	/**
	 * @return the queryClass
	 */
	public Class<B> getQueryClass() {
		return queryClass;
	}

	/**
	 * @param queryClass
	 *            the queryClass to set
	 */
	public void setQueryClass(Class<B> queryClass) {
		this.queryClass = queryClass;
	}

}
