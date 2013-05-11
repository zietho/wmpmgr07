package com.web.util;

public class WebUtils {

	public static String formattedTimeFromMinutes(Integer minutes){
		String ret="  kürzer als";
		
		return ret+ formattedTimeFromMinutes1(minutes);
	}
	
	public static String formattedTimeFromMinutes1(Integer minutes){
		String ret="  ";
		int hours=0;
		
		if(minutes!=null){
			hours=minutes/60;
			minutes=minutes%60;
			
			if(hours>0){
				ret+=hours+(hours == 1 ? " Stunde " : " Stunden ");
			}
			
			if(minutes > 0)
				ret+=minutes+(minutes == 1 ? " Minute " : " Minuten ");
		}
		
		return ret;
	}
	
}
