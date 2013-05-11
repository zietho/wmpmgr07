package com.util.geolocation.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.constants.Constants;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;

public class LocationMainTest extends TestCase {
	
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
	public void test_getLocation(){
		try {
			MyGeoPoint loc=LocationAPI.getLocation("Österreich, Karlsplatz 13, 1040 Wien");
			
			if(loc!=null){
				assertTrue((loc.getLatitude()>48 && loc.getLatitude()<49) && (loc.getLongitude()>16 && loc.getLongitude()<17));
			}
			else{
				assertTrue(false);
			}
			
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
	@Test
	public void test_getAddress(){
		try {
			MyGeoPoint loc=new MyGeoPoint(48.1989798, 16.3699039);
			String text=LocationAPI.getAdress(loc);
			
			if(text!=null && text.length()>0){
				assertTrue(text.contains("Wien") && text.contains("Karlsplatz") && text.contains("Österreich") && text.contains("1040"));
			}
			else{
				assertTrue(false);
			}
			
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
	
	@Test
	public void test_getDistance(){
		try {
			String dist=LocationAPI.getDistance("Wien Österreich 1010","Salzburg Österreich 5010",LocationAPI.MODE_DRIVING);
						
			if(dist!=null && dist.length()>0){
				String distLength=dist.split(" ")[0];
				Long length=Long.parseLong(distLength);
				assertTrue(length>250);
			}
			else{
				assertTrue(false);
			}
			
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
	
	@Test
	public void test_getDistanceInMeter(){
		try {
			MyGeoPoint gp=new MyGeoPoint(48.309627,16.612015);
			double dist=LocationAPI.getDistanceInMeter(Constants.VIENNA_CENTRE, gp);
			
			logger.debug("--> "+dist);
			
			assertTrue(dist<23000);
			
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
	@Test
	public void test_isInViennaTrue(){
		try {
			MyGeoPoint gp=new MyGeoPoint(48.267594,16.458206);
			assertTrue(LocationAPI.isInVienna(gp));	
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
	
	@Test
	public void test_isInViennaFalse(){
		try {
			MyGeoPoint gp=new MyGeoPoint(48.383557,16.757584);
			assertFalse(LocationAPI.isInVienna(gp));	
		} catch (Throwable e) {
			assertTrue(false);
		}	
	}
	
}
