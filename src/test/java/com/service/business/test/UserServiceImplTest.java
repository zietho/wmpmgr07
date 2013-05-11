package com.service.business.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.wienerhelden.BaseUnitTest;

import com.database.bean.User;
import com.service.business.BusinessService;


public class UserServiceImplTest extends BaseUnitTest{

	@Inject
	private BusinessService businessService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	public void simpleLoginTest() {
		assertTrue(businessService.createUser("Hans","Zimmer","hanszi5","test@wh.at", "test"));
		User user = businessService.login("hanszi5", "test");
		assertTrue(user != null);
		businessService.deleteUser(user);
	}
	
	@Test
	public void longPasswordLoginTest() {
		assertTrue(businessService.createUser("Hans", "Zimmer", "hanszi5", "test@wh.at", "qwertzuiopüasdfghjklöä<yxcvbnm,.-1234567dfghjtrzuid 6ftgzhuji"));
		User user = businessService.login("hanszi5", "qwertzuiopüasdfghjklöä<yxcvbnm,.-1234567dfghjtrzuid 6ftgzhuji");
		assertTrue(user != null);
		businessService.deleteUser(user);
	}
	
	@Test
	public void invalidPasswordTest() {
		assertTrue(businessService.createUser("Hans","Zimmer","hanszi5","test@wh.at", "test"));
		User user = businessService.login("hanszi5", "t");
		assertTrue(user == null);
		user = businessService.login("hanszi5", "test");
		assertTrue(user != null);
		businessService.deleteUser(user);
	}
	
	@Test
	public void createUsernameIsNull() {
		assertFalse(businessService.createUser("Hans", "Zimmer", null, null, "test"));
	}
	
	@Test
	public void createPasswordIsNull() {
		assertFalse(businessService.createUser("Hans", "Zimmer", "hanszi2", "test2@wh.at", null));
	}
	
	@Test
	public void createAndLoginCompare() {
		businessService.createUser("Hans", "Zimmer", "hanszi", "test@wh.at", "helloWorld");
		User user = businessService.login("hanszi", "helloWorld");
		assertEquals("hanszi", user.getNickname());
		businessService.deleteUser(user);
	}
	
	@Test
	public void getAllUser() {
		assertFalse(businessService.getAllUsers().isEmpty());
	}
	
	@Test
	public void userProfileNull() {
		assertFalse(businessService.changeProfilePicture(null, null));
	}
}
