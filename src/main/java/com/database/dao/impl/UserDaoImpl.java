package com.database.dao.impl;

import javax.inject.Named;

import org.hibernate.Session;

import com.database.bean.User;
import com.database.dao.UserDao;

@Named("userDao")
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}
	
	@Override
	public User getUserByNickname(String nickname) {
		Session session = this.getSessionFactory().openSession();
		User user=(User) session.createQuery("from User where nickname = :nickname").setParameter("nickname", nickname).uniqueResult();
		session.close();
		
		return user;
		
	}

}
