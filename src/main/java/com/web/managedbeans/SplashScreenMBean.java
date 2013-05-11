package com.web.managedbeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Controller;

@ManagedBean(name="splashScreenMBean")
@RequestScoped 
@Controller
public class SplashScreenMBean extends AbstractManagedBean implements Serializable {
	private static int MAX = 8;
	private static int MIN = 1;
	private static int BRIGHT = 1;
	private static int DARK = 2; 
	
	private int generatedRandomNumber = 0; 
	private int colorSchema = 0;
	private int c = 0; 
	private int bright[] = {4,5,6,7};
	private int dark[] = {1,2,3,8};
	
	
	public SplashScreenMBean(){
		
	}
	
	public String getBackground(){
		FacesContext ctx = FacesContext.getCurrentInstance();
		String path = ctx.getExternalContext().getRequestContextPath();
		return path+"/resources/img/splashbg/"+getRandomId()+".jpg";
	}
	
	public int getRandomId(){
		//Random Id erzeugen
		int randomId = generateRandomNumber(MIN,MAX,generatedRandomNumber);
		this.generatedRandomNumber = randomId; 
		logger.debug("ereugte zufallszahl"+generatedRandomNumber);
		
		//color schema setzen
		for(int id:bright){
			if(id==this.generatedRandomNumber){
				this.colorSchema = DARK;
			}
		}
		
		for(int id:dark){
			if(id==this.generatedRandomNumber){
				this.colorSchema = BRIGHT;
			}
		}
		
		
		
		return randomId; 
	}
	
	public String getColorSchema(){
		return (this.colorSchema==1) ? "bright" : "dark";
	}
		
	public int generateRandomNumber(int min, int max, int before){
		int random = min + (int)(Math.random() * ((max - min) + 1));
		if(random!=before){
			return random;
		}else{
			return generateRandomNumber(min, max, before);
		}
	}
	
}
