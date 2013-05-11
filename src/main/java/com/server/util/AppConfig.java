package com.server.util;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.Thanks;
import com.database.bean.User;
import com.database.bean.Workflow;

@Configuration
public class AppConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	@Bean(name="transactionManager")
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory());
	}


	@Bean
	public SessionFactory sessionFactory() {
		logger.debug("instantiate SessionFactory");
		LocalSessionFactoryBuilder beanFactory = new LocalSessionFactoryBuilder(
				dataSource);
		beanFactory.addAnnotatedClasses(Address.class, Category.class,
				Sotivity.class, Thanks.class, User.class, Workflow.class, Skill.class);
		beanFactory.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQLDialect");
		beanFactory.setProperty("hibernate.hbm2ddl", "validate");
		beanFactory.setProperty("hibernate.search.lucene_version",
				"LUCENE_CURRENT");
		SessionFactory sf = beanFactory.buildSessionFactory();
		logger.debug("build sf: {}", sf);
		return sf;
	}
	
}
