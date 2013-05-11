package com.service.business.impl;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.database.bean.Thanks;
import com.database.dao.ThanksDao;
import com.service.business.ThanksService;

@Service
public class ThanksServiceImpl implements ThanksService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ThanksDao thanksDao;
	
	@Override
	public void createThanks(Thanks thanks) {
		thanksDao.create(thanks);
	}



}
