package com.service.business.test;

import static org.junit.Assert.*;

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

import com.database.bean.Address;
import com.database.bean.Sotivity;
import com.service.business.BusinessService;

public class AddressServiceImplTest extends BaseUnitTest {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	public void test_createAddress() {
		List<Address> add=businessService.searchAddresses();
		int size=add.size();
		
		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setPlace("Krumbach");

		businessService.createAddress(adr);
		
		add=businessService.searchAddresses();
		
		assertTrue((size+1)==add.size());
	}
	
	
	@Test
	public void test_deleteAddress() {

		List<Address> add=businessService.searchAddresses();
		int size=add.size();
		
		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setPlace("Krumbach");

		businessService.createAddress(adr);
		
		businessService.deleteAddress(adr);
		
		add=businessService.searchAddresses();
		
		assertTrue((size)==add.size());
	}
	
}
