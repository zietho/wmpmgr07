package com.web.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SlideEndEvent;
import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;
import com.web.util.WebUtils;

@ManagedBean(name = "sotivityMBean")
@SessionScoped
@Controller
public class SotivityMBean extends AbstractManagedBean {

	private static final long serialVersionUID = 1L;

	private String title;

	private String description;

	private int visibility;
	
	private int groupSotivity;

	private Date dateEnd;

	private String street;

	private String zip;

	private String place;

	private Double lat;

	private Double lng;

	private User actualUser;
	
	private Integer duration=null;
	
	private String skills;

	private Map<Integer, Category> categoryMap;
	private Map<Integer, Skill> skillMap;
	
	private List<SelectItem> categoryList = new ArrayList<SelectItem>();
	//private List<SelectItem> skillList = new ArrayList<SelectItem>();
	private  Map<String,String> skillList;
	
	private Integer selectedCategory;
	private List<String> selectedSkills;
	
	private Category choosenCategory;
	private List<Skill> choosenSkills;

	private List<Category> categories;
	private List<Skill> allSkills;  

	private String formattedDuration=WebUtils.formattedTimeFromMinutes1(duration);
	
	private Sotivity actualSotivity;
	
	private boolean changeMode;
	
	public SotivityMBean() {
		super();
		this.place = "Wien";
		this.zip = "1";
		this.visibility = 1;
		this.groupSotivity = 2;
		
		this.initUser();
		
		this.initSotivity();
		
		if (!this.changeMode) {
			this.initCategories();
			this.initSkills();
		}
	}

	/**
	 * Holt den aktuellen User aus Session
	 */
	private void initUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		this.actualUser = (User) httpSession.getAttribute("user");
	}

	/**
	 * Liste an Categories initialisieren. Liste von DB holen. Außerdem
	 * SelectItems fuer Anzeige befuellen.
	 */
	private void initCategories() {
		categories = businessService.getAllCategories();

		categoryMap = new HashMap<Integer, Category>();

		categoryList.add(new SelectItem(null, "Auswahl treffen"));
		for (Category cat : categories) {
			categoryMap.put(cat.getId(), cat);
			categoryList.add(new SelectItem(cat.getId(), cat.getTitle()));
		}
	}
	
	/**
	 * Liste an Skills initialisieren. Liste von DB holen. Außerdem
	 * SelectItems fuer Anzeige befuellen.
	 */
	private void initSkills() {
		allSkills = businessService.getAllSkills();
		//allSkills=this.getTestSkills();
		
		logger.debug("Skills: " + allSkills);
		
		skillMap = new HashMap<Integer, Skill>();
		skillList = new HashMap<String, String>();  

		for (Skill sk : allSkills) {
			skillMap.put(sk.getId(), sk);
			skillList.put(sk.getTitle(), sk.getId().toString());
		}
		
	}
	
	public List<Skill> getTestSkills(){
		List<Skill> testSkills = new ArrayList<Skill>();
		Skill sk = new Skill();
		sk.setId(1);
		sk.setTitle("EDV Kenntnisse");
		testSkills.add(sk);
		
		sk = new Skill();
		sk.setId(2);
		sk.setTitle("handwerklich begabt");
		testSkills.add(sk);
		
		sk = new Skill();
		sk.setId(3);
		sk.setTitle("kann gut mit Kindern");
		testSkills.add(sk);
		
		sk = new Skill();
		sk.setId(4);
		sk.setTitle("kann gut mit älteren Personen");
		testSkills.add(sk);
		
		return testSkills;
	}

	public void clearFields() {
		this.title = null;
		this.description = null;
		// this.visibility=null;
		this.dateEnd = null;
		this.street = null;
		this.zip = null;
		this.place = null;
		this.lat = null;
		this.lng = null;
		this.choosenCategory = null;
		this.selectedCategory = null;
		this.skills = null;
		this.duration = null;
	}

	/**
	 * Vorbedingungen prüfen
	 * 
	 * @return
	 */
	public boolean checkPreConditions() {

		boolean allCorrect = true;

		if (this.selectedCategory == null || this.selectedCategory == 0) {
			allCorrect = false;
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Bitte wählen Sie eine Kategorie aus", ""));
		}

		if (this.dateEnd == null) {
			allCorrect = false;
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Bitte wählen Sie eine Enddatum aus!", ""));
		}

		return allCorrect;
	}
	
	public void initSotivity(){
		this.changeMode=false;
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		this.actualSotivity = (Sotivity) httpSession.getAttribute("sotivity");
		
		if(this.actualSotivity!=null){
			this.initSotivityFields();
			this.changeMode=true;
		}
	}
	
	/**
	 * Seite bei Aenderungsmodus mit Sotivity befuellen
	 */
	public void initSotivityFields(){
		
		//als erstes Sotivity von DB holen/laden
		//damit sollten auch alle anderen Listen da sein
		//businessService.getSotivityById(idfromlink);
		Sotivity sotivity = this.actualSotivity;
		
		//alle Listen wie ueblich befuellen
		this.initCategories();
		this.initSkills();
		this.initUser();
		
		//Adressdaten setzen
		Address adr = sotivity.getAddress();
		this.street = adr.getStreet();
		this.place = adr.getPlace();
		this.zip = "" + adr.getZip();
		this.lat = adr.getLatitude();
		this.lng = adr.getLongitude();
		
		this.title = sotivity.getTitle();
		this.description = sotivity.getDescription();
		this.dateEnd = sotivity.getDateEnd();

		if (sotivity.isPublicVisible()){
			this.visibility = 1;
		}else{
			this.visibility = 2;
		}
		
		if (sotivity.isGroupSotivity()){
			this.groupSotivity = 1;
		}else{
			this.groupSotivity = 2;
		}
		
		this.duration = sotivity.getDuration();
		this.skills = sotivity.getSkills();
				
		for(Category cat: sotivity.getSotivityCategories()){
			this.selectedCategory = cat.getId();
		}
		
		this.selectedSkills = new ArrayList<String>();
		for(Skill sk : sotivity.getSotivitySkills()){
			this.selectedSkills.add(sk.getId().toString());
		}
		
	}

	/**
	 * Sotivity erstellen
	 */
	public String createSotivity() {
		String navigationReturn = null;
		boolean publicVisible = false;
		boolean groupSot = false;
		String filename = null;
		Address adr = null;

		if (this.checkPreConditions()) {


			if (this.getVisibility() == 1) {
				publicVisible = true;
			}
			
			if (this.getGroupSotivity() == 1) {
				groupSot = true;
			}

			HashSet<Category> sotivityCategories = null;
			if (this.selectedCategory != null) {
				this.choosenCategory = categoryMap.get(selectedCategory);
				sotivityCategories = new HashSet<Category>();
				sotivityCategories.add(choosenCategory);
			}
			
			HashSet<Skill> sotivitySkills = null;
			Skill sSkill = null;
			if (this.selectedSkills != null && this.selectedSkills.size() > 0) {
				sotivitySkills = new HashSet<Skill>();
				for (String si : this.selectedSkills) {
					sSkill = skillMap.get(Integer.valueOf(si));
					sotivitySkills.add(sSkill);
				}
			}

			this.setAddressLatLong();

			adr = new Address();
			adr.setName("Sotivityadresse");
			adr.setStreet(street);
			adr.setZip(Integer.parseInt(zip));
			adr.setPlace(place);
			adr.setLatitude(this.lat);
			adr.setLongitude(this.lng);

			this.initUser();

			Sotivity sotivity = new Sotivity();
			sotivity.setTitle(this.getTitle());
			sotivity.setDescription(this.getDescription());
			sotivity.setDateEnd(dateEnd);
			sotivity.setPublicVisible(publicVisible);
			sotivity.setUser(this.actualUser);
			sotivity.setDuration(this.getDuration());
			sotivity.setSkills(this.getSkills());
			sotivity.setGroupSotivity(groupSot);

			if (sotivityCategories != null) {
				sotivity.setSotivityCategories(sotivityCategories);
			}
			
			if (sotivitySkills != null) {
				sotivity.setSotivitySkills(sotivitySkills);
			}

			businessService.createAddress(adr);
			sotivity.setAddress(adr);

			businessService.createSotivity(sotivity);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sotivity erstellt. ID=" + sotivity.getId(), ""));

			this.actualUser.getOwnedSotivities().add(sotivity);
			businessService.updateUser(this.actualUser);

			this.logger.info("Sotivity erstellt. ID=" + sotivity.getId());

			navigationReturn = "userSotivities?faces-redirect=true";

			this.clearFields();
		} else {
			navigationReturn = null;
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sotivity-Erstellung aufgrund von Fehlern fehlgeschlagen",
					""));
			this.logger
					.info("Sotivityerstellung aufgrund fehlender Preconditions nicht moegliche");
		}

		return navigationReturn;

	}
	
	/**
	 * Sotivity erstellen
	 */
	public String updateSotivity() {
		String navigationReturn = null;
		boolean publicVisible = false;
		boolean groupSot = false;
		String filename = null;
		Address adr = null;

		if (this.checkPreConditions()) {


			if (this.getVisibility() == 1) {
				publicVisible = true;
			}
			
			if (this.getGroupSotivity() == 1) {
				groupSot = true;
			}

			HashSet<Category> sotivityCategories = null;
			if (this.selectedCategory != null) {
				this.choosenCategory = categoryMap.get(selectedCategory);
				sotivityCategories = new HashSet<Category>();
				sotivityCategories.add(choosenCategory);
			}
			
			HashSet<Skill> sotivitySkills = null;
			Skill sSkill = null;
			if (this.selectedSkills != null && this.selectedSkills.size() > 0) {
				sotivitySkills = new HashSet<Skill>();
				for (String si : this.selectedSkills) {
					sSkill = skillMap.get(Integer.valueOf(si));
					sotivitySkills.add(sSkill);
				}
			}

			this.setAddressLatLong();

			adr = new Address();
			adr.setName("Sotivityadresse");
			adr.setStreet(street);
			adr.setZip(Integer.parseInt(zip));
			adr.setPlace(place);
			adr.setLatitude(this.lat);
			adr.setLongitude(this.lng);

			this.initUser();

			this.actualSotivity.setTitle(this.getTitle());
			this.actualSotivity.setDescription(this.getDescription());
			this.actualSotivity.setDateEnd(dateEnd);
			this.actualSotivity.setPublicVisible(publicVisible);
			this.actualSotivity.setUser(this.actualUser);
			this.actualSotivity.setDuration(this.getDuration());
			this.actualSotivity.setSkills(this.getSkills());
			this.actualSotivity.setGroupSotivity(groupSot);

			if (sotivityCategories != null) {
				this.actualSotivity.setSotivityCategories(sotivityCategories);
			}
			
			if (sotivitySkills != null) {
				this.actualSotivity.setSotivitySkills(sotivitySkills);
			}

			businessService.createAddress(adr);
			this.actualSotivity.setAddress(adr);

			businessService.updateSotivity(this.actualSotivity);
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sotivity geändert. ID=" + this.actualSotivity.getId(), ""));

			navigationReturn = "userSotivities?faces-redirect=true";

			this.clearFields();
		} else {
			navigationReturn = null;
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Sotivity-Änderung aufgrund von Fehlern fehlgeschlagen",
					""));
			this.logger
					.info("Sotivityänderung aufgrund fehlender Preconditions nicht moegliche");
		}

		return navigationReturn;

	}

	

	private void setAddressLatLong() {
		MyGeoPoint point = null;
		if (this.street != null || this.zip != null || this.place != null) {
			try {
				point = LocationAPI.getLocation(this.street + " " + this.zip
						+ " " + this.place);
			} catch (Exception e) {
				point = null;
			}
		}
		if (point != null) {

			this.setLat(point.getLatitude());
			this.setLng(point.getLongitude());

			this.logger.info("Address found");
		}
	}
	
	public void formatDuration(SlideEndEvent event) {  
        this.formattedDuration= WebUtils.formattedTimeFromMinutes1(event.getValue());
        this.duration=event.getValue();
    } 
	 
	
	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	/**
	 * @return the choosenCategory
	 */
	public Category getChoosenCategory() {
		return choosenCategory;
	}

	/**
	 * @param choosenCategory
	 *            the choosenCategory to set
	 */
	public void setChoosenCategory(Category choosenCategory) {
		this.choosenCategory = choosenCategory;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the visibility
	 */
	public int getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility
	 *            the visibility to set
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street
	 *            the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place
	 *            the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}

	/**
	 * @return the dateEnd
	 */
	public Date getDateEnd() {
		return dateEnd;
	}

	/**
	 * @param dateEnd
	 *            the dateEnd to set
	 */
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	/**
	 * @return the categoryList
	 */
	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	/**
	 * @param categoryList
	 *            the categoryList to set
	 */
	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	/**
	 * @return the categoryMap
	 */
	public Map<Integer, Category> getCategoryMap() {
		return categoryMap;
	}

	/**
	 * @param categoryMap
	 *            the categoryMap to set
	 */
	public void setCategoryMap(Map<Integer, Category> categoryMap) {
		this.categoryMap = categoryMap;
	}

	/**
	 * @return the selectedCategory
	 */
	public Integer getSelectedCategory() {
		return selectedCategory;
	}

	/**
	 * @param selectedCategory
	 *            the selectedCategory to set
	 */
	public void setSelectedCategory(Integer selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	/**
	 * @return the actualUser
	 */
	public User getActualUser() {
		return actualUser;
	}

	/**
	 * @param actualUser
	 *            the actualUser to set
	 */
	public void setActualUser(User actualUser) {
		this.actualUser = actualUser;
	}

	/**
	 * @return the pictureFile
	 */
	

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		formattedDuration=WebUtils.formattedTimeFromMinutes1(duration);
		this.duration = duration;
	}

	/**
	 * @return the skills
	 */
	public String getSkills() {
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(String skills) {
		this.skills = skills;
	}

	/**
	 * @return the skillMap
	 */
	public Map<Integer, Skill> getSkillMap() {
		return skillMap;
	}

	/**
	 * @param skillMap the skillMap to set
	 */
	public void setSkillMap(Map<Integer, Skill> skillMap) {
		this.skillMap = skillMap;
	}

	/**
	 * @return the skillList
	 */
	public Map<String, String> getSkillList() {
		return skillList;
	}

	/**
	 * @param skillList the skillList to set
	 */
	public void setSkillList(Map<String, String> skillList) {
		this.skillList = skillList;
	}

	/**
	 * @return the selectedSkills
	 */
	public List<String> getSelectedSkills() {
		return selectedSkills;
	}

	/**
	 * @param selectedSkills the selectedSkills to set
	 */
	public void setSelectedSkills(List<String> selectedSkills) {
		this.selectedSkills = selectedSkills;
	}

	/**
	 * @return the choosenSkills
	 */
	public List<Skill> getChoosenSkills() {
		return choosenSkills;
	}

	/**
	 * @param choosenSkills the choosenSkills to set
	 */
	public void setChoosenSkills(List<Skill> choosenSkills) {
		this.choosenSkills = choosenSkills;
	}

	/**
	 * @return the allSkills
	 */
	public List<Skill> getAllSkills() {
		return allSkills;
	}

	/**
	 * @param allSkills the allSkills to set
	 */
	public void setAllSkills(List<Skill> allSkills) {
		this.allSkills = allSkills;
	}

	public String getFormattedDuration() {
		return formattedDuration;
	}

	public void setFormattedDuration(String formattedDuration) {
		this.formattedDuration = formattedDuration;
	}

	public Sotivity getActualSotivity() {
		return actualSotivity;
	}

	public void setActualSotivity(Sotivity actualSotivity) {
		this.actualSotivity = actualSotivity;
	}

	public boolean isChangeMode() {
		return changeMode;
	}

	public void setChangeMode(boolean changeMode) {
		this.changeMode = changeMode;
	}

	public int getGroupSotivity() {
		return groupSotivity;
	}

	public void setGroupSotivity(int groupSotivity) {
		this.groupSotivity = groupSotivity;
	}

}
