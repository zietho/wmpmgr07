package com.web.managedbeans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.User;


@ManagedBean(name="loginMBean")
@RequestScoped
@Controller
public class LoginMBean extends AbstractManagedBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nickname;
	private String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String submit() {
		if(nickname==null || nickname.equals("") || password==null || password.equals(""))
			return "";
		
		logger.info("login "+nickname);

		User user = businessService.login(nickname, password);
		
		if(user != null) {
			logger.info("logged in with "+user.getFirstname()+" "+user.getLastname());

			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
			HttpSession httpSession = request.getSession(false);
			httpSession.setAttribute("user", user);
			
			//addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "",""));
			
			nickname = null;
			password = null;
		
			return "mainLoggedIn?faces-redirect=true";
		}
		else {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username oder Passwort nicht richtig",""));
			return "";
		}
	}
	
	public String goToCreateSotivity(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.removeAttribute("sotivity");
		httpSession.removeAttribute("sotivityMBean");
		
		return "sotivity?faces-redirect=true";
	}
}
