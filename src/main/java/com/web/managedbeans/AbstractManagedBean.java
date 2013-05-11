package com.web.managedbeans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.server.util.ServiceUtil;
import com.service.business.BusinessService;

@Controller
public abstract class AbstractManagedBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	public MapModel emptyModel=new DefaultMapModel();
	
	
//	@Autowired
//	@ManagedProperty(value="#{businessService}")
	protected BusinessService businessService;
	
	
	public AbstractManagedBean() {
		businessService = ServiceUtil.getInstance().getService();
	}
	
	
	/**
	 * Falls ein Marker bewegt wurde
	 */
	 public void onMarkerDrag(MarkerDragEvent event) {  
	       Marker marker = event.getMarker();  
	          
	       addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Punkt neu platziert", ""));  
	 }
	 
		
		public MapModel getEmptyModel() {
			if(emptyModel==null || emptyModel.getMarkers()==null || emptyModel.getMarkers().size()==0)
				emptyModel = new DefaultMapModel();
			
			return emptyModel;
		}
		
		public void setEmptyModel(MapModel emptyModel) {
			this.emptyModel = emptyModel;
		}
	
}
