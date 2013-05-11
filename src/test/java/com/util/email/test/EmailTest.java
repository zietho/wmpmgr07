package com.util.email.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.util.email.Email;

public class EmailTest {

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

	@Test(timeout=1000)
	public void sendEmailTest() {
		Email email = new Email("Testmail", "Von WienerHeöäülden.at", "christian.fischer@FILLINstudent.tuwien.ac.at", "christian.fischer@FILLINstudent.tuwien.ac.at");
	}
}
