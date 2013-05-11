package com.database.dao.impl;

import javax.inject.Named;

import com.database.bean.Thanks;
import com.database.dao.ThanksDao;

@Named("thanksDao")
public class ThanksDaoImpl extends BaseDaoImpl<Thanks> implements ThanksDao {

	public ThanksDaoImpl() {
		super(Thanks.class);
	}
	
}
