package com.util.geolocation;

import java.io.Serializable;

public class MyGeoPoint implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double latitude=0;
	private double longitude=0;
	
	public MyGeoPoint(double latitude, double longitude){
		this.latitude=latitude;
		this.longitude=longitude;
	}


	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "MyGeoPoint [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
	
	
}
