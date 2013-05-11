package com.util.geolocation;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.constants.Constants;
import com.google.common.collect.Maps;


/**
 * Quellen
 * https://developers.google.com/maps/documentation/directions/?hl=de
 * http://acadopus.de/java/beispiele-fuer--arbeit-mit-google-map-api-in-java_5930.html
 * 
 * @author Markus
 *
 */

public class LocationAPI {

	public static final String MODE_WALKING="walking";
	public static final String MODE_DRIVING="driving";
	public static final String MODE_BICYCLING="bicycling";
	
	private static Logger logger = LoggerFactory.getLogger(LocationAPI.class);

	/**
	 * Liefert zu übergebenen Longitutde und Latitude Werten die Daten der Adresse
	 * @param point
	 * @return
	 * @throws Exception
	 */
	public static String getAdress(MyGeoPoint point) throws Exception{
		
		if(point!=null){
			final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// Path zu  Geocoding API per HTTP
		    
			final Map<String, String> language = Maps.newHashMap();
		    language.put("language","de");// die Sprache für Antwort
		    
		    final Map<String, String> sensor = Maps.newHashMap();
		    sensor.put("sensor","false");// ob das Gerät GPS-Sensor hat
		    
		    final Map<String, String> position = Maps.newHashMap();
		    position.put("latlng",point.getLatitude()+","+point.getLongitude()); //Position
		    
		    final String url = baseUrl + "?" + encodeParam(position)+"&"+encodeParam(sensor)+"&"+encodeParam(language); // Parametern generieren
		    final JSONObject response = JsonReader.read(url);// Anfrage an Service und seine Antwort
		    
		    // //results[0]/formatted_address
		    final JSONObject location = response.getJSONArray("results").getJSONObject(0);
		    final String formattedAddress = location.getString("formatted_address");
	
		    return formattedAddress;
		}
		else{
			throw new Exception("Es muss Geolokation angegeben werden.");
		}
	}
	
	
	/**
	 * Liefert zu einer übergebenen Adresse die Longitude und Latitude Information
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static MyGeoPoint getLocation(String address) throws Exception{
		final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// Path zu Geocoding API per HTTPs
		
		if(address!=null){
			final Map<String, String> sensor = Maps.newHashMap();
		    sensor.put("sensor", "false");// ob das Gerät GPS-Sensor hat
			
		    final Map<String, String> addressP = Maps.newHashMap();
		    addressP.put("address", address);// die Adresse		
		    
		    final String url = baseUrl + '?' + encodeParam(addressP)+"&"+encodeParam(sensor);// Parametern generieren
		    final JSONObject response = JsonReader.read(url);// Anfrage an Service und seine Antwort
	
		    JSONObject resultObject = response.getJSONArray("results").getJSONObject(0);
		    
		    JSONObject location = resultObject.getJSONObject("geometry");
		    location = location.getJSONObject("location");
	
		    final double lng = location.getDouble("lng");// longitude
		    final double lat = location.getDouble("lat");// latitude
		    
		    MyGeoPoint g= new MyGeoPoint(lat, lng);
	    	
		    return g;
		}
		else{
			throw new Exception("Es muss Adresse angegeben werden.");
		}
	    
	}
	
	/**
	 * Gibt die Distanz als String zurück zu zwei übergebenen Adressen 
	 * dazu wird noch der Mode angegeben
	 * @param addressFrom
	 * @param addressTo
	 * @param mode
	 * @return
	 * @throws Exception
	 */
	public static String getDistance(String addressFrom,String addressTo,String mode) throws Exception{

		if(mode==null || !(mode.equals(MODE_BICYCLING) || mode.equals(MODE_WALKING) || mode.equals(MODE_DRIVING))){
			mode=MODE_WALKING;
		}
		
		
		if(addressFrom!=null && addressTo!=null){
			final String baseUrl = "http://maps.googleapis.com/maps/api/directions/json";// Path zu Geocoding API per
		    // HTTP
		    final Map<String, String> language = Maps.newHashMap();
		    language.put("language", "de");// die Sprache für Antwort
		    
		    final Map<String, String> sensor = Maps.newHashMap();
		    sensor.put("sensor", "false");// ob das Gerät GPS-Sensor hat
		    
		    final Map<String, String> modeP = Maps.newHashMap();
		    modeP.put("mode",mode); //Position
		    
		    final Map<String, String> fromAdd = Maps.newHashMap();
		    fromAdd.put("origin",addressFrom); //Position
		    
		    final Map<String, String> toAdd = Maps.newHashMap();
		    toAdd.put("destination",addressTo); //Position
		    
		    final String url = baseUrl + "?" + encodeParam(fromAdd)+"&"+encodeParam(toAdd)+"&"+
		    encodeParam(modeP)+"&"+encodeParam(sensor)+"&"+encodeParam(language);
		    
		    // Parametern generieren
		    logger.debug(url); 
		    final JSONObject response = JsonReader.read(url);// Anfrage an Service und seine Antwort
		    
		    // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
		    JSONObject location = response.getJSONArray("routes").getJSONObject(0);
		    location = location.getJSONArray("legs").getJSONObject(0);
		    final String distance = location.getJSONObject("distance").getString("text");
		    //final String duration = location.getJSONObject("duration").getString("text");
		    
		    return distance;
		}
		else{
			throw new Exception("Es müssen Ziel- und Quelladresse angegeben werden.");
		}
	}

	
	/**
	 * Wandelt einen übergebenen Parameter in eine URL encodierte Form um
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String encodeParam(final Map<String, String> param) throws UnsupportedEncodingException {
	    StringBuilder builder=new StringBuilder();
		if(param!=null){
	    	Set<String> keys=param.keySet();
	    	Iterator<String> it=keys.iterator();
	    	
	    	while(it.hasNext()){
	    		String paramStr=it.next();
	    		builder.append(paramStr);
	    		builder.append('=');
	    		String value=param.get(paramStr);
	    		builder.append(URLEncoder.encode(value, "utf-8"));
	    	}

	    	String encodedParams=builder.toString();
	    	
	    	return encodedParams;
	    }
		
	    return null;
	}
	
	
	
	/**
	 * Calculates geodetic distance between two points specified by latitude/longitude using Vincenty inverse formula
	 * for ellipsoids
	 * 
	 * http://de.softuses.com/12174
	 * 
	 * @param lat1 first point latitude in decimal degrees
	 * @param lon1 first point longitude in decimal degrees
	 * @param lat2 second point latitude in decimal degrees
	 * @param lon2 second point longitude in decimal degrees
	 * @returns distance in meters between points with 5.10<sup>-4</sup> precision
	 * @see <a href="http://www.movable-type.co.uk/scripts/latlong-vincenty.html">Originally posted here</a>
	 */
	public static double getDistanceInMeter(MyGeoPoint gp1, MyGeoPoint gp2) {
		
		double lat1=gp1.getLatitude();
		double lon1=gp1.getLongitude();
		double lat2=gp2.getLatitude();
		double lon2=gp2.getLongitude();
		
	    double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563; // WGS-84 ellipsoid params
	    double L = Math.toRadians(lon2 - lon1);
	    double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
	    double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
	    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
	    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

	    double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
	    double lambda = L, lambdaP, iterLimit = 100;
	    do {
	        sinLambda = Math.sin(lambda);
	        cosLambda = Math.cos(lambda);
	        sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
	        if (sinSigma == 0)
	            return 0; // co-incident points
	        cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
	        sigma = Math.atan2(sinSigma, cosSigma);
	        sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
	        cosSqAlpha = 1 - sinAlpha * sinAlpha;
	        cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
	        if (Double.isNaN(cos2SigmaM))
	            cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
	        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
	        lambdaP = lambda;
	        lambda = L + (1 - C) * f * sinAlpha
	                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
	    } while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

	    if (iterLimit == 0)
	        return Double.NaN; // formula failed to converge

	    double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
	    double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
	    double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
	    double deltaSigma = B
	            * sinSigma
	            * (cos2SigmaM + B
	                    / 4
	                    * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
	                            * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
	    double dist = b * A * (sigma - deltaSigma);

	    return dist;
	}
	
	
	/**
	 * Üperprüft ob sich der übergebene Punkt innerhalb Wiens befindet
	 * @param p
	 * @return
	 */
	public static boolean isInVienna(MyGeoPoint p){
		double distance=getDistanceInMeter(Constants.VIENNA_CENTRE,p);
		
		if(distance<=Constants.CIRCLE_RADIUS)
			return true;
		
		return false;
	}
	
	
}
