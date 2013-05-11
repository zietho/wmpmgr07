package com.web.managedbeans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.User;

@ManagedBean(name = "showSotivityMBean")
@SessionScoped
@Controller
public class ShowSotivityMBean extends AbstractManagedBean {

	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private int publicVisible;
	private Date dateCreated;
	private Date dateEnd;
	private User owner;
	private List<Category> categories;
	private Address address;
	
	private User actualUser;

	public ShowSotivityMBean() {
		super();
		initUser();
	}
	
	/**
	 * Holt den aktuellen User aus Session
	 */
	private void initUser(){	
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		this.actualUser = (User) httpSession.getAttribute("user");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPublicVisible() {
		return publicVisible;
	}

	public void setPublicVisible(int publicVisible) {
		this.publicVisible = publicVisible;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public User getActualUser() {
		return actualUser;
	}

	public void setActualUser(User actualUser) {
		this.actualUser = actualUser;
	}
}
