package com.web.managedbeans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.web.util.WebUtils;

@ManagedBean(name = "mainLoggedInMBean")
@SessionScoped
@Controller
public class MainLoggedInMBean extends AbstractManagedBean {

	private static final long serialVersionUID = 1L;

	// alle Sotivities
	private MapModel allSotivityModel = new DefaultMapModel();

	Marker marker;
	Sotivity selectedSotivity;
	List<Sotivity> sotivities;
	private Hashtable<Integer, Sotivity> sotivitiesLookUp;
	private List<Skill> selectedSotivitySkills; 

	/**
	 * 
	 */
	public MainLoggedInMBean() {
		super();
		allSotivityModel = new DefaultMapModel();
		sotivities = businessService.getSotivities();
		sotivitiesLookUp = new Hashtable<Integer, Sotivity>();
		selectedSotivity = new Sotivity();

		for (Sotivity sot : sotivities) {
			Address add = sot.getAddress();
			String title = "";
			String id = "";

			if (sot.getTitle() != null) {
				title = sot.getTitle();
				id = sot.getId().toString();
			}

			if (add != null && add.getLatitude() != null
					&& add.getLongitude() != null) {
				Marker marker = new Marker(new LatLng(add.getLatitude(),
						add.getLongitude()), id);
				allSotivityModel.addOverlay(marker);
			}

			// for later use and direct access
			sotivitiesLookUp.put(sot.getId(), sot);
		}

	}

	public Sotivity getSelectedSotivity() {
		return selectedSotivity;
	}

	public void setSelectedSotivity(Sotivity selectedSotivity) {
		this.selectedSotivity = selectedSotivity;
	}

	public MapModel getAllSotivityModel() {
		return allSotivityModel;
	}

	public void setAllSotivityModel(MapModel allSotivityModel) {
		this.allSotivityModel = allSotivityModel;
	}

	public List<Sotivity> getAllSotivities() {
		return businessService.getSotivities();
	}

	public void onMarkerSelect(OverlaySelectEvent event) {

		marker = (Marker) event.getOverlay();
		int id = Integer.parseInt(marker.getTitle());
		logger.debug("id of selected sotivity: " + id);
		logger.debug("size of hashtable " + sotivitiesLookUp.size());
		selectedSotivity = lookUpSotivityById(id);
		if (selectedSotivity == null) {
			logger.debug("null!");
		}
		else{
			logger.debug("selected id: " + selectedSotivity.getId() + " title: "
				+ selectedSotivity.getTitle());
		}

	}

	public Marker getMarker() {
		return marker;
	}

	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Sotivity lookUpSotivityById(int id) {
		if (sotivitiesLookUp.containsKey(id)) {
			logger.debug("in");
			return sotivitiesLookUp.get(id);
		}

		return null;
	}

	public List<Skill> getSelectedSotivitySkills() {
		selectedSotivitySkills = new ArrayList(selectedSotivity.getSotivitySkills());
		logger.debug("skills of sotivity: "+selectedSotivitySkills.toString());
		return selectedSotivitySkills;
	}

	public void setSelectedSotivitySkills(List<Skill> selectedSotivitySkills) {
		this.selectedSotivitySkills = selectedSotivitySkills;
	}
	
	public String getSelectedSotivityDuration(){
		return WebUtils.formattedTimeFromMinutes1(selectedSotivity.getDuration());
	}

	
	
}
