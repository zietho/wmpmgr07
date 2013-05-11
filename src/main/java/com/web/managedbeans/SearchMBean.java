package com.web.managedbeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.bean.Workflow;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;
import com.web.util.WebUtils;

@ManagedBean(name = "searchMBean")
@SessionScoped
@Controller
public class SearchMBean extends AbstractManagedBean {
	private static final long serialVersionUID = -9151305376329523237L;
	private String query = "";
	private List<Sotivity> result;
	private Sotivity selectedSotivity;
	private String offerComment;
	private User user;

	private Map<Integer, Address> addressMap;
	private List<SelectItem> addressList = new ArrayList<SelectItem>();
	private Integer selectedAddress;

	private Map<Integer, Category> categoryMap;
	private List<SelectItem> categoryList = new ArrayList<SelectItem>();
	private Integer selectedCategory;
	
	private Map<Integer, Skill> skillMap;
	private List<SelectItem> skillList = new ArrayList<SelectItem>();
	private Integer selectedSkill;
	
	private String addressText=null;
	private MyGeoPoint gp=null;
	
	private double lat;
	private double lng;
	
	private Date dateEnd=null;
	private int sliderRange=100; //bezeichnet den Radius
	private boolean extSearchActivated;
	private Integer maxDuration=60;
	private String formattedDuration=WebUtils.formattedTimeFromMinutes(maxDuration);

	public SearchMBean() {
		super();
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		setQuery((String) httpSession.getAttribute("q"));	


		//setResult(businessService.searchSotivitiesUltimate(null, null, null, null, null, null));
		List<Sotivity> result=businessService.searchSotivitiesUltimate(null, null, null, null,null, null,null, this.query);
		setResult(result);
		
		addSearchPoint();

		initUserAddresses();
		initCategories();
		initSkills();
	}

	public User getUser() {
		if (user == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context
					.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			user = (User) httpSession.getAttribute("user");
		}

		return user;
	}

	private void initUserAddresses() {
		List<Address> addresses = new ArrayList<Address>(this.getUser()
				.getUserAddresses());

		addressMap = new HashMap<Integer, Address>();

		addressList.add(new SelectItem(null, "Auswahl treffen"));
		for (Address adr : addresses) {
			addressMap.put(adr.getId(), adr);
			addressList.add(new SelectItem(adr.getId(), adr.getName()));
		}
	}

	private void initCategories() {
		List<Category> categories = businessService.getAllCategories();

		categoryMap = new HashMap<Integer, Category>();

		categoryList.add(new SelectItem(null, "Auswahl treffen"));
		for (Category cat : categories) {
			categoryMap.put(cat.getId(), cat);
			categoryList.add(new SelectItem(cat.getId(), cat.getTitle()));
		}
	}
	
	private void initSkills() {
		List<Skill> skills = businessService.getAllSkills();

		skillMap = new HashMap<Integer, Skill>();

		skillList.add(new SelectItem(null, "Auswahl treffen"));
		for (Skill ski : skills) {
			skillMap.put(ski.getId(), ski);
			skillList.add(new SelectItem(ski.getId(), ski.getTitle()));
		}
	}

	public List<Sotivity> getResult() {
		return result;
	}

	public void setResult(List<Sotivity> result) {
		this.result = result;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Sotivity getSelectedSotivity() {
		return selectedSotivity;
	}

	public void setSelectedSotivity(Sotivity selectedSotivity) {
		this.selectedSotivity = selectedSotivity;
	}

	public String getOfferComment() {
		return offerComment;
	}

	public void setOfferComment(String offerComment) {
		this.offerComment = offerComment;
	}

	
	public void searchSimple() {
		List<Sotivity> result=businessService.searchSotivitiesUltimate(null, null, null, null,null,null, null, query);
		setResult(result);
	}
		
	
	public void formatDuration(SlideEndEvent event) {  
        this.formattedDuration= WebUtils.formattedTimeFromMinutes(event.getValue());
    } 
	
	public void deleteSearchPoint(){
		this.gp=null;
		addSearchPoint();
	}
	
	public void searchReset() {
		this.gp=null;
		this.sliderRange=100;
		this.maxDuration=60;
		this.selectedCategory=0;
		this.selectedAddress=0;
		this.selectedSkill=0;
		this.dateEnd=null;
		this.extSearchActivated=false;
		search();
	}
	
	
	public void search() {

		// selektierte Category von DropDown-Box
		Category choosenCategory = null;
		if (this.selectedCategory != null && this.selectedCategory>0) {
			choosenCategory = categoryMap.get(selectedCategory);
			logger.debug("Kategorie ausgewählt {}" , selectedCategory);
		}else{
			logger.debug("Keine Kategorie ausgewählt");
		}
		
		Skill choosenSkill = null;
		if (this.selectedSkill != null && this.selectedSkill>0) {
			choosenSkill = skillMap.get(selectedSkill);
			logger.debug("Skill ausgewählt {}" , selectedSkill);
		}else{
			logger.debug("Kein Skill ausgewählt");
		}
		
	
		Double lon=null;
		Double lat=null;
		Double range=null;
		
		if(this.gp!=null){
			lon=this.gp.getLongitude();
			lat=this.gp.getLatitude();
			range=((double)this.sliderRange/1000); //Range von Kilometer in Meter
		}
		else{
			lon=null;
			lat=null;
			range=null;
		}
		
		Integer sMaxDuration=null;
		Date sDatEnd = dateEnd;

		if(this.extSearchActivated){ //nur verwenden wenn aktiviert
			sMaxDuration=maxDuration;
		}
		
		
		List<Sotivity> result=businessService.searchSotivitiesUltimate(lat, lon, range, choosenCategory,choosenSkill, sDatEnd,sMaxDuration, query);
		setResult(result);
		
		addSearchPoint();
	}

	public boolean offerSent(Sotivity sotivity) {
		for (Workflow w : sotivity.getWorkflows()) {
			if (w.getUser().getId().equals(getUser().getId()))
				return true;
		}

		return false;
	}
	
	public boolean isOfferable(Sotivity sotivity) {
		if(sotivity.getUser().getId().equals(getUser().getId()))
			return false;
		else if(offerSent(sotivity))
			return false;
		else
			return true;
	}

	public void sendOffer() {
		logger.debug(offerComment);

		businessService.sendOffer(getUser(), selectedSotivity, offerComment);

		search();
		
		this.offerComment = "";
	}

	public Map<Integer, Address> getAddressMap() {
		return addressMap;
	}

	public void setAddressMap(Map<Integer, Address> addressMap) {
		this.addressMap = addressMap;
	}

	public List<SelectItem> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<SelectItem> addressList) {
		this.addressList = addressList;
	}

	public Integer getSelectedAddress() {
		return selectedAddress;
	}

	public void setSelectedAddress(Integer selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public Map<Integer, Category> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(Map<Integer, Category> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public Integer getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(Integer selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	
	public Map<Integer, Skill> getSkillMap() {
		return skillMap;
	}

	public void setSkillMap(Map<Integer, Skill> skillMap) {
		this.skillMap = skillMap;
	}

	public List<SelectItem> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<SelectItem> skillList) {
		this.skillList = skillList;
	}

	public Integer getSelectedSkill() {
		return selectedSkill;
	}

	public void setSelectedSkill(Integer selectedSkill) {
		this.selectedSkill = selectedSkill;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	/**
	 * Befüllt das MapModel abhängig von den vorhandenen Daten (Suchpunkt und Suchergebnisse)
	 */
	private void addSearchPoint(){
		emptyModel=new DefaultMapModel();
		
		if(gp!=null){  //Suchpunkt
			Marker marker = new Marker(new LatLng(gp.getLatitude(), gp.getLongitude()), null,null,"http://maps.google.com/mapfiles/ms/micons/blue-dot.png");
			marker.setDraggable(true);
			emptyModel.addOverlay(marker);
			
			Circle umkreis = new Circle(new LatLng(gp.getLatitude(), gp.getLongitude()), this.sliderRange);  
			umkreis.setStrokeColor("#00ff00");  
			umkreis.setFillColor("#00ff00");  
			umkreis.setStrokeOpacity(0.3);  
            umkreis.setFillOpacity(0.3); 
            emptyModel.addOverlay(umkreis);
		}
		
		if(result!=null){ //Suchergebnisse
			for(Sotivity sot : result){
				if(sot.getAddress()!=null && sot.getAddress().getLongitude()!=null && sot.getAddress().getLatitude()!=null){
					Marker marker = new Marker(new LatLng(sot.getAddress().getLatitude(),sot.getAddress().getLongitude()), null);
					emptyModel.addOverlay(marker);
				}
			}
		}
		
	}
	
	
	public void onSlideEnd(SlideEndEvent event) {  
        this.sliderRange=event.getValue();  
        this.addSearchPoint();
    }
	
	
	/**
	 * Falls ein Marker bewegt wurde
	 */
	 public void onMarkerDragSearchPoint(MarkerDragEvent event) {  
	       Marker marker = event.getMarker();  
	       
	       this.gp=new MyGeoPoint(marker.getLatlng().getLat(), marker.getLatlng().getLng());
	       
	       addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Suchort neu platziert", ""));  
	       
	       addSearchPoint();
	 }
	 
	
	public void addAddress(ActionEvent event) {
		
		if(this.addressText!=null && this.addressText.length()>0){
			try {
				this.gp=LocationAPI.getLocation(this.addressText);	
				
				addSearchPoint();
				
				this.selectedAddress=0;
				
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Suchort gesetzt",""));
				
				try {
					this.addressText=LocationAPI.getAdress(this.gp);
				} catch (Exception e) {
					this.addressText="keine Adresse gefunden";
				}
				
		} catch (Exception e) {
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Adresse kann nicht ermittelt werden.\nService steht nicht zur Verfügung",""));
			}
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Adresse kann nicht ermittelt werden.",""));
		}
	}

	public String getAddressText() {
		return addressText;
	}

	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}
	
	/**
	 * Adresse mit Klick auf Map auswählen
	 * @return
	 */
	public String addMarker() {	
		this.gp=new MyGeoPoint(this.lat, this.lng);
		
		if(LocationAPI.isInVienna(this.gp)){
			addSearchPoint();
			
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Suchort gesetzt",""));
			
			this.selectedAddress=0;
			
			try {
				this.addressText=LocationAPI.getAdress(this.gp);
			} catch (Exception e) {
				this.addressText="keine Adresse gefunden";
			}
		}
		else{
			this.gp=null;
			addMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Suchort muss sich innerhalb von Wien befinden!",""));
		}
		
		return "";
	}
	
	
	/**
	 * Setzen der Adresse mittels DropDown
	 * @param event
	 */
	public void onOwnAddressChange(AjaxBehaviorEvent event){
		if(this.selectedAddress!=null && this.selectedAddress!=0){
			Address add=this.addressMap.get(this.getSelectedAddress());
			
			this.gp=new MyGeoPoint(add.getLatitude(), add.getLongitude());
			
			if(LocationAPI.isInVienna(this.gp)){
				addSearchPoint();
				
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Suchort gesetzt",""));
				
				try {
					this.addressText=LocationAPI.getAdress(this.gp);
				} catch (Exception e) {
					this.addressText="keine Adresse gefunden";
				}
			}
			else{
				this.gp=null;
				addMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Suchort muss sich innerhalb von Wien befinden!",""));
			}
		}
		else{
			logger.debug("No Element Choosen");
		}
	}
	

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public int getSliderRange() {
		return sliderRange;
	}

	public void setSliderRange(int sliderRange) {
		this.sliderRange = sliderRange;
	}

	public Integer getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(Integer maxDuration) {
		formattedDuration=WebUtils.formattedTimeFromMinutes(maxDuration);
		this.maxDuration = maxDuration;
	}

	public boolean isExtSearchActivated() {
		return extSearchActivated;
	}

	public void setExtSearchActivated(boolean extSearchActivated) {
		this.extSearchActivated = extSearchActivated;
	}

	public String getFormattedDuration() {
		return formattedDuration;
	}

	public void setFormattedDuration(String formattedDuration) {
		this.formattedDuration = formattedDuration;
	}

}
