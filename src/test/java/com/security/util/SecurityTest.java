package com.security.util;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.constants.Constants;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;

public class SecurityTest extends TestCase {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void test_Hashing(){
		String testString="test";
		
		byte[] hash=SecurityUtil.stringToHash(testString);
		String str=SecurityUtil.hashToString(hash);
		
		assertTrue(testString.equals(str));
	}
	
	
	@Test
	public void test_Authenticate(){
		String digest="tQzmQvyrkMcTAkcB11TN7HQPf9I=";
		String password="test1";
		String salt="69d6Fe2PqFU=";
		
		try {
			assertTrue(SecurityUtil.authenticate(digest, password, salt));
		} catch (NoSuchAlgorithmException e) {
			assertTrue(false);
		} catch (UnsupportedEncodingException e) {
			assertTrue(false);
		}
	}
	
	
	public void test_SaltIsRandom(){
		byte[] salt1;
		byte[] salt2;
		
		try {
			salt1 = SecurityUtil.createSalt();
			salt2=SecurityUtil.createSalt();
			
			assertTrue(!salt1.equals(salt2));
		} catch (NoSuchAlgorithmException e) {
			assertTrue(false);
		}
	}
	
}
