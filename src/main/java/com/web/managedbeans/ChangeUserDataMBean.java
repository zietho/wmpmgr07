package com.web.managedbeans;

import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.security.util.SecurityUtil;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;

@ManagedBean(name="changeUserMBean")
@SessionScoped
@Controller
public class ChangeUserDataMBean extends AbstractManagedBean {
	
	private int MAXIMUM_MARKERS=5;
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private User changeUser=null;
	
	private String firstname=null;
	private String lastname=null;
	private String nickname=null;
	private String email=null;
	private String newPassword=null;
	
	private String addressText=null;
	private String addressTextTitle=null;
	private MyGeoPoint gp=null;
	

	private UploadedFile file;
	private StreamedContent pictureFile;
	   
	//Adressdaten
	private String pointTitle;
	private double lat;
	private double lng;
	
	private Map<Integer, Category> categoryMap;
	private List<String> selectedCategories;
	private List<Category> choosenCatgories;
	private  Map<String,String> categories;  
	
	public ChangeUserDataMBean() {
		super();
		getUser();
		
		if(this.user!=null){
			Set<Address> addresses=this.user.getUserAddresses();
			if(addresses!=null){
				for(Address address : addresses){
					if(address!=null){
						Marker marker = new Marker(new LatLng(address.getLatitude(), address.getLongitude()), address.getName());
						marker.setDraggable(true);
						emptyModel.addOverlay(marker);
					}
				}
			}
		}
		
		this.initCategories();
	}


	public User getUser() {
		logger.trace("Entered get User");
		if(this.user == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			this.user = (User) httpSession.getAttribute("user");
			
			if(this.user!=null){
				this.firstname=this.user.getFirstname();
				this.lastname=this.user.getLastname();
				this.nickname=this.user.getNickname();
				this.email=this.user.getEmail();
			}
			
		}
		
		return this.user;
	}
	
	/**
	 * Liste an Categories initialisieren. Liste von DB holen. Außerdem
	 * SelectItems fuer Anzeige befuellen.
	 */
	private void initCategories() {
		List<Category> cats = businessService.getAllCategories();

		categoryMap = new HashMap<Integer, Category>();
		
		categories = new HashMap<String, String>();  

		for (Category cat : cats) {
			categoryMap.put(cat.getId(), cat);
			categories.put( cat.getTitle(), cat.getId().toString()); 
		}
		
		if(this.user!=null){
			this.selectedCategories = new ArrayList<String>();
			if (this.user.getUserCategories() != null) {
				for (Category cat : this.user.getUserCategories()) {
					this.selectedCategories.add(cat.getId().toString());
				}
			}
		}
		 
	}
	
	
	/**
	 * Userdaten ändern
	 * @return
	 */
	public String submitUserData() {
		logger.trace("Entered submitData");
		
		if(this.user!=null){
			
			this.user.setFirstname(this.firstname);
			this.user.setLastname(this.lastname);
			this.user.setNickname(this.nickname);
			this.user.setEmail(this.email);
			
			businessService.updateUser(this.user);
			
			//neuen User in Session schreiben
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.invalidate();
			
			httpSession = request.getSession(true);
			httpSession.setAttribute("user", this.user);
			
			logger.info("User Data changed "+this.user);
			
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Daten geändert!",""));
			
			return "";
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Kein User eingelogged!",""));
		}
		
		return "";
	}
	
	/**
	 * Categories ändern
	 * @return
	 */
	public String submitCategories() {
		logger.trace("Entered submitCategories");
		
		if(this.user!=null){
			
			HashSet<Category> userCategories = new HashSet<Category>();
			Category cat = null;
			
			if (this.selectedCategories != null && this.selectedCategories.size() > 0) {
				//userCategories = new HashSet<Category>();
				for (String si : this.selectedCategories) {
					cat = categoryMap.get(Integer.valueOf(si));
					userCategories.add(cat);
				}
			}
			this.user.setUserCategories(userCategories);
			
			businessService.updateUser(this.user);
			
			//neuen User in Session schreiben
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.invalidate();
			
			httpSession = request.getSession(true);
			httpSession.setAttribute("user", this.user);
			
			logger.info("User Data changed "+this.user);
			
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Daten geändert!",""));
			
			return "";
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Kein User eingelogged!",""));
		}
		
		return "";
	}  
	
	/**
	 * Passwort ändern
	 * @return
	 */
	public String submitPassword() {
		logger.trace("Enterd submitPassword");
		logger.info("change it");
		
		if(this.user!=null){
			
			String salt=null;
			String newPasswd=null;
			try {
				byte[] bDigest=null;
				byte[] saltByte=SecurityUtil.createSalt();
				salt=SecurityUtil.hashToString(saltByte);
			
				bDigest = SecurityUtil.getHash(this.newPassword, saltByte);
				newPasswd=SecurityUtil.hashToString(bDigest);
		
			} catch (Exception e) {
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Fehler beim Passwort ändern",""));
				return "";
			}
			
			this.user.setPassword(newPasswd);
			this.user.setSalt(salt);
			
			businessService.updateUser(this.user);
			
			//neuen User in Session schreiben
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.invalidate();
			
			httpSession = request.getSession(true);
			httpSession.setAttribute("user", this.user);
			
			logger.info("User Data changed "+this.user);
			
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Passwort geändert",""));
			
			return "";
			
			
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Kein User eingelogged!",""));
		}
		
		return "";
	}
	
	
	public String submitLocations() {
		
		if(this.user!=null && this.emptyModel!=null){
			List<Marker> markers=emptyModel.getMarkers();
			List<Address> changedAddress=new Vector<Address>();
			 
			//Für Referenz auf die User
			Set<User> refuser=new HashSet<User>();
			refuser.add(this.user);
			
			//alte Addressen entfernen
			for(Address add : this.user.getUserAddresses()){
				
				//nur löschen wenn die Adresse an keiner Sotivity oder mehreren Usern hängt
				if((add.getSotivities()==null && add.getSotivities().size()==0) && (add.getUsers()==null || add.getUsers().size()==1))
					this.businessService.deleteAddress(add);
			}
			
			//aus HashSet löschen
			this.user.getUserAddresses().clear();
			
			//neue Adressen erzeugen und hinzufügen
			for(Marker marker : markers){
				Address address=new Address();
				address.setUsers(refuser);  //der eigenen User ist refuser
				address.setLatitude(marker.getLatlng().getLat());
				address.setLongitude(marker.getLatlng().getLng());
				address.setName(marker.getTitle());
				address.setSotivities(new HashSet<Sotivity>()); //leer
				address.setPlace("");
				address.setZip(0);
				address.setStreet("");
				Date d=new Date();
				address.setDateCreated(new Timestamp(d.getTime()));
				changedAddress.add(address);
				
				//hinzufügen und User updaten
				this.user.getUserAddresses().add(address);
				businessService.updateUser(this.user);
			}
			
			
			//neue Addressen als HashSet nochmal speichern
			this.user.addNewAddresses(changedAddress);	
			businessService.updateUser(this.user);
			
					
			//neuen User in Session schreiben
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.invalidate();
			
			httpSession = request.getSession(true);
			httpSession.setAttribute("user", this.user);
			
			logger.info("User Data changed "+this.user);
			
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Lokationen gespeichert",""));
					
			return "";   //"userProfil?faces-redirect=true"
		}
		
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Lokationen konnten nicht übernommen werden",""));
		
		return "";
	}
	
	public void addAddress(ActionEvent event) {
		
		if(this.addressText!=null && this.addressText.length()>0){
			try {
				this.gp=LocationAPI.getLocation(this.addressText);				
		} catch (Exception e) {
				addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Adresse kann nicht ermittelt werden.\nService steht nicht zur Verfügung",""));
			}
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Adresse kann nicht ermittelt werden.",""));
		}
	}
	
	public void resetValues(ActionEvent actionEvent) {	
		this.gp=null;
	}
	
	public String addMarker() {	
		
		//wenn der Punkt mittels Adresse übersetzt wird
		if(this.gp!=null){
			this.lat=gp.getLatitude();
			this.lng=gp.getLongitude();
		}
		
		if(emptyModel!=null){
			Marker marker = new Marker(new LatLng(this.lat, this.lng), pointTitle);
			marker.setDraggable(true);
			
			//Prüfung ob noch Marker hinzugefügt werden dürfen
			if(emptyModel.getMarkers().size()>=MAXIMUM_MARKERS){   //Grenze fuer maximale Anzahl an Markers
				
				if(MAXIMUM_MARKERS==0)
					addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Es ist keine Ortsangabe zulässig.",""));
				else if(MAXIMUM_MARKERS==1)
					addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Es ist maximal ein Ort zulässig.",""));
				else
					addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Es sind maximal "+MAXIMUM_MARKERS+" Orte zulässig.",""));
			}
			else{
				MyGeoPoint p=new MyGeoPoint(this.lat, this.lng);
				
				if(LocationAPI.isInVienna(p)){  //ist Punkt in Wien dann hinzufügen
					emptyModel.addOverlay(marker);
					
					addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Ort hinzugefuegt",""+pointTitle));
					this.pointTitle="";
				}
				else{
					addMessage(new FacesMessage(FacesMessage.SEVERITY_WARN, "Ort muss sich innerhalb von Wien befinden!",""));
					this.pointTitle="";
				}
			}
		}
		else{
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Ort konnte nicht hinzugefuegt werden",""));
		}
		
		this.gp=null;
		return "";
	}

	/**
	 * Liste aller Marker
	 * @return
	 */
	public List<Marker> getAllMarkers() {
		return emptyModel.getMarkers();
	}


	/**
	 * Property Action Listener
	 * @param selectedMarker - Marker aus der Liste
	 */
	public void setSelectedMarker(Marker selectedMarker) {
		this.emptyModel.getMarkers().remove(selectedMarker);
		
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Ort entfernt.",""+selectedMarker.getTitle()));
	}
	

	public void setUser(User user) {
		this.user = user;
	}


	public User getChangeUser() {
		return changeUser;
	}


	public void setChangeUser(User changeUser) {
		this.changeUser = changeUser;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getNickname() {
		return nickname;
	}


	public void setNickname(String nickname) {
		this.nickname = nickname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getPointTitle() {
		return pointTitle;
	}


	public void setPointTitle(String pointTitle) {
		this.pointTitle = pointTitle;
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


	public String getAddressText() {
		return addressText;
	}


	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getAddressTextTitle() {
		return addressTextTitle;
	}


	public void setAddressTextTitle(String addressTextTitle) {
		this.addressTextTitle = addressTextTitle;
	}


	/**
	 * File uploaden
	 * 
	 * @param event
	 */
	public void handleFileUpload(FileUploadEvent event) {

		this.file = event.getFile();

		if (file != null) {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "File: "
					+ file.getFileName() + " wurde erfolgreich hochgeladen.",
					""));

			this.logger.info("File: " + file.getFileName()
					+ " wurde erfolgreich hochgeladen.");
			businessService.changeProfilePicture(getUser(), file);
		} else {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Beim Hochladen der Datei sind Probleme aufgetreten.", ""));

			this.logger
					.error("Beim Hochladen der Datei sind Probleme aufgetreten.");
		}

	}
	
	public StreamedContent getPictureFile() {

		if (this.file != null) {
			this.pictureFile = new DefaultStreamedContent(
					new ByteArrayInputStream(file.getContents()), "image/jpg");
		}

		return pictureFile;
	}

	/**
	 * @param pictureFile
	 *            the pictureFile to set
	 */
	public void setPictureFile(StreamedContent pictureFile) {
		this.pictureFile = pictureFile;
	}
	

	/**
	 * @return the file
	 */
	public UploadedFile getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(UploadedFile file) {
		this.file = file;
	}


	/**
	 * @return the categoryMap
	 */
	public Map<Integer, Category> getCategoryMap() {
		return categoryMap;
	}


	/**
	 * @param categoryMap the categoryMap to set
	 */
	public void setCategoryMap(Map<Integer, Category> categoryMap) {
		this.categoryMap = categoryMap;
	}

	/**
	 * @return the selectedCategories
	 */
	public List<String> getSelectedCategories() {
		return selectedCategories;
	}


	/**
	 * @param selectedCategories the selectedCategories to set
	 */
	public void setSelectedCategories(List<String> selectedCategories) {
		this.selectedCategories = selectedCategories;
	}


	/**
	 * @return the categories
	 */
	public Map<String, String> getCategories() {
		return categories;
	}


	/**
	 * @param categories the categories to set
	 */
	public void setCategories(Map<String, String> categories) {
		this.categories = categories;
	}


	/**
	 * @return the choosenCatgories
	 */
	public List<Category> getChoosenCatgories() {
		return choosenCatgories;
	}


	/**
	 * @param choosenCatgories the choosenCatgories to set
	 */
	public void setChoosenCatgories(List<Category> choosenCatgories) {
		this.choosenCatgories = choosenCatgories;
	}
	
}
