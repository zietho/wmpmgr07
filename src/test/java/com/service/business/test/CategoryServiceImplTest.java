package com.service.business.test;

import static org.junit.Assert.assertFalse;

import javax.inject.Inject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.wienerhelden.BaseUnitTest;

import com.database.bean.Address;
import com.database.bean.Sotivity;
import com.service.business.BusinessService;

public class CategoryServiceImplTest extends BaseUnitTest {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	private BusinessService businessService;

	@Test
	public void getAllCatgoriessTest() {
		assertFalse(businessService.getAllCategories().isEmpty());
	}
}
