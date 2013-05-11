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

public class SotivityServiceImplTest extends BaseUnitTest {

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
	public void test_createSotivity() {

		List<Sotivity> list = businessService.getSotivities();
		int anz = list.size();

		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setPlace("Krumbach");

		Sotivity sotivity = new Sotivity();
		sotivity.setTitle("titletest");
		sotivity.setDescription("Descriptiontest");
		// sotivity.setUser(this.actualUser);
		// sotivity.setPicture(filename);

		// businessService.createAddress(adr);
		sotivity.setAddress(adr);

		businessService.createAddress(adr);
		sotivity.setAddress(adr);
		businessService.createSotivity(sotivity);

		list = businessService.getSotivities();

		assertTrue((anz + 1) == list.size());
	}

	@Test
	public void test_deleteSotivity() {

		List<Sotivity> list = businessService.getSotivities();
		int anz = list.size();

		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setPlace("Krumbach");

		Sotivity sotivity = new Sotivity();
		sotivity.setTitle("titletest");
		sotivity.setDescription("Descriptiontest");
		// sotivity.setUser(this.actualUser);
		// sotivity.setPicture(filename);

		// businessService.createAddress(adr);
		sotivity.setAddress(adr);

		businessService.createAddress(adr);
		sotivity.setAddress(adr);
		businessService.createSotivity(sotivity);

		businessService.deleteSotivity(null, sotivity);

		list = businessService.getSotivities();

		assertTrue(anz == list.size());
	}

	@Test
	public void test_searchUltimate() {

		List<Sotivity> list = businessService.getSotivities();

		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setLatitude(16.1d);
		adr.setLongitude(48.1d);
		adr.setPlace("Krumbach");

		Sotivity sotivity = new Sotivity();
		sotivity.setTitle("titletest");
		sotivity.setDescription("Descriptiontest");
		// sotivity.setUser(this.actualUser);
		// sotivity.setPicture(filename);

		// businessService.createAddress(adr);
		sotivity.setAddress(adr);

		businessService.createAddress(adr);
		sotivity.setAddress(adr);
		businessService.createSotivity(sotivity);

		List<Sotivity> sot = businessService.searchSotivitiesUltimate(16.1,
				48.1, 1d, null, null, null, null, null);

		assertTrue(sot.size() > 0);
	}

	@Test
	public void test_searchUltimateFail() {

		List<Sotivity> list = businessService.getSotivities();

		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setLatitude(16.1d);
		adr.setLongitude(48.1d);
		adr.setPlace("Krumbach");

		Sotivity sotivity = new Sotivity();
		sotivity.setTitle("titletest");
		sotivity.setDescription("Descriptiontest");
		// sotivity.setUser(this.actualUser);
		// sotivity.setPicture(filename);

		// businessService.createAddress(adr);
		sotivity.setAddress(adr);

		businessService.createAddress(adr);
		sotivity.setAddress(adr);
		businessService.createSotivity(sotivity);

		List<Sotivity> sot = businessService.searchSotivitiesUltimate(16.1,
				90.1, 1d, null, null, null, null, null);

		assertFalse(sot.size() > 0);
	}

	@Test
	public void test_SotivityUpdate() {

		List<Sotivity> list = businessService.getSotivities();

		List<Sotivity> sot = businessService.searchSotivitiesUltimate(null,
				null, null, null, null, null, null, "Test123");
		assertTrue(sot.size() == 0);

		Address adr = new Address();
		adr.setStreet("Feldgasse 8");
		adr.setZip(2851);
		adr.setLatitude(16.1d);
		adr.setLongitude(48.1d);
		adr.setPlace("Krumbach");

		Sotivity sotivity = new Sotivity();
		sotivity.setTitle("titletest");
		sotivity.setDescription("Descriptiontest");
		// sotivity.setUser(this.actualUser);
		// sotivity.setPicture(filename);

		// businessService.createAddress(adr);
		sotivity.setAddress(adr);

		businessService.createAddress(adr);
		sotivity.setAddress(adr);
		businessService.createSotivity(sotivity);

		sot = businessService.searchSotivitiesUltimate(null, null, null, null,
				null, null, null, "titletest");
		assertTrue(sot.size() > 0);

		sotivity.setTitle("Test123");
		businessService.updateSotivity(sotivity);

		sot = businessService.searchSotivitiesUltimate(null, null, null, null,
				null, null, null, "Test123");

		assertTrue(sot.size() > 0);
        businessService.deleteSotivity(null,sotivity);
	}

	@Test(expected = Exception.class)
	public void fbTestFail() {
		businessService.publishToFacebook(null);
	}

	@Test(expected = Exception.class)
	public void fbTestFailRequest() {
		Sotivity soti = !businessService.getSotivities().isEmpty() ? businessService
				.getSotivities().get(0) : null;
		businessService.publishToFacebook(soti);
	}
}
