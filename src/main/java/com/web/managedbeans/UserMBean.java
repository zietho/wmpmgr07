package com.web.managedbeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.User;

@ManagedBean(name="userMBean")
@SessionScoped
@Controller
public class UserMBean extends AbstractManagedBean {
	
	private static final long serialVersionUID = 1L;
	
	private User user;
	private String firstname = "";
	private String lastname = "";

	public UserMBean() {
		super();
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

	public User getUser() {
		if(user == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			user = (User) httpSession.getAttribute("user");
			if(user != null) firstname = user.getFirstname();
			if(user != null) lastname = user.getLastname();
		}
		
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.invalidate();
		this.user = null;
		return "logoff?faces-redirect=true";
	}
	
	
	public String getGreeting(){
		getUser();
		if(this.firstname!=null && this.firstname.length()>=1){
			return "Hallo, "+this.firstname;
		}

		return "Hallo";
	}
	
}
