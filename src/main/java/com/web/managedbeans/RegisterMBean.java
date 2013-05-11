package com.web.managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.User;


@ManagedBean(name="registerMBean")
@SessionScoped
@Controller
public class RegisterMBean extends AbstractManagedBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String firstname;
	private String lastname;
	private String nickname;
	private String email;
	private String password;
	
	private boolean success = false;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String submit() {
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Welcome " + firstname + " " + lastname + "!"));
		
		//TODO prüfen ob Nickname und Email schon vorhanden
		
		logger.trace("Enterd submit");
		this.setSuccess(true);
		
		
		//Nickname schon verwendet
		if(businessService.isNicknameUsed(nickname)){
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Nickname bereits vorhanden",""));
			return "";
		}
			
		
		success = businessService.createUser(firstname, lastname, nickname, email, password);
		logger.info("performed create");
//		if(success)
//			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "User erstellt",""));
//		else
//			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Leider ein Fehler beim User erstellen.",""));

		User user = businessService.login(nickname, password);
		
		if(user != null) {
			logger.info("logged in with "+user.getFirstname()+" "+user.getLastname());
			firstname = null;
			lastname = null;
			nickname = null;
			email = null;
			password = null;
			
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("user", user);
		return "userTour?faces-redirect=true";
		}
		else {
			return "";
		}
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
