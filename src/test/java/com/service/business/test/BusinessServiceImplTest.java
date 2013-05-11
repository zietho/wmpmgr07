package com.service.business.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.wienerhelden.BaseUnitTest;

import com.database.bean.Skill;
import com.service.business.BusinessService;

public class BusinessServiceImplTest extends BaseUnitTest{

	
	@Inject
	private BusinessService businessService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//TODO set autocommit false!
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void getAllSkillsTest() {
		List<Skill> allSkills = businessService.getAllSkills();
		logger.debug("Skills: " + allSkills);
		
		assertNotNull(allSkills);
		assertTrue(allSkills.size() > 0);
	}

}
