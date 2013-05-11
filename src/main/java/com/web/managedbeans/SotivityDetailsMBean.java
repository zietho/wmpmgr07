package com.web.managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import org.apache.cxf.bus.BusState;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Sotivity;

@ManagedBean(name = "sotivityDetailsMBean")
@RequestScoped
@Controller
public class SotivityDetailsMBean extends AbstractManagedBean {
	
	private MapModel mapModel;  
	private Sotivity sotivity;
	
	@ManagedProperty(value = "#{userMBean}")
	private UserMBean user; 
	
	public SotivityDetailsMBean(){
		mapModel = new DefaultMapModel(); 
	}
	
	public String init(int id){
		sotivity = businessService.getSotivityById(id);
		Address add = sotivity.getAddress();
		String title = "";
		
		if (sotivity.getTitle() != null) {
			title = sotivity.getTitle();
		}

		if (add != null && add.getLatitude() != null
				&& add.getLongitude() != null) {
			Marker marker = new Marker(new LatLng(add.getLatitude(),
					add.getLongitude()), title);
			mapModel.addOverlay(marker);
		}
		
        return "sotivityDetails.xhtml";
	}
	
	public Sotivity getSotivity() {
		return sotivity;
	}

	public void setSotivity(Sotivity sotivity) {
		this.sotivity = sotivity;
	}

	public boolean isOwnerOf(){
		return (user.getUser().equals(sotivity.getUser())) ? true : false; 
	}
	
	public MapModel getMapModel() {  
		return mapModel;  
	}

	public UserMBean getUser() {
		return user;
	}

	public void setUser(UserMBean user) {
		this.user = user;
	}  
	
	
	
}
