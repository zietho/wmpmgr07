package com.web.managedbeans;

import org.primefaces.component.datatable.*;
import org.primefaces.event.RowEditEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.Sotivity;
import com.database.bean.User;
import com.util.models.SotivityDataModel;
import com.util.models.UserDataModel;

@ManagedBean(name = "moderatorMBean")
@SessionScoped
@Controller
public class ModeratorMBean extends AbstractManagedBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<Sotivity> sotivities;
	private List<Sotivity> filteredSotivities; 
	private Sotivity selectedSotivity;
	private Sotivity[] selectedSotivities;
	private SotivityDataModel sotivityModel;

	private List<User> users;
	private List<User> filteredUsers; 
	private User selectedUser;
	private User[] selectedUsers;
	private UserDataModel userModel;	
	
	private User currentUser; 

	public ModeratorMBean() {
		sotivities = new ArrayList<Sotivity>();
		sotivities = businessService.getSotivities();
		sotivityModel = new SotivityDataModel(sotivities);

		users = new ArrayList<User>();
		users = businessService.getAllUsers();
		userModel = new UserDataModel(users);
	}
	
	/**
	 * Holt den aktuellen User aus Session
	 */
	private void initUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		this.currentUser = (User) httpSession.getAttribute("user");
	}

	/*
	 * * SOTIVITIES
	 */
	public Sotivity[] getSelectedSotivities() {
		return selectedSotivities;
	}

	public void setSelectedSotivities(Sotivity[] selectedSotivities) {
		this.selectedSotivities = selectedSotivities;
	}

	public Sotivity getSelectedSotivity() {
		return selectedSotivity;
	}

	public void setSelectedSotivity(Sotivity selectedSotivity) {
		this.selectedSotivity = selectedSotivity;
	}

	public SotivityDataModel getSotivityModel() {
		return sotivityModel;
	}
	
	public void deleteSotivity() {
		if (this.selectedSotivity != null) {
			deleteSingleSotivity(this.selectedSotivity);
		} else if (this.selectedSotivities.length > 0) {
			deleteMultipleSotivities(this.selectedSotivities);
		}
		
		logger.debug("selekt soti:"+selectedSotivities.length);

		this.updateSotivityModel();
	}

	public void deleteSingleSotivity(Sotivity sotivity) {
		logger.debug("deleting Sotivity with name " + sotivity.getTitle());
		businessService.deleteSotivity(currentUser, sotivity);
	}

	public void deleteMultipleSotivities(Sotivity[] sotivities) {
		for (Sotivity sotivity : sotivities) {
			deleteSingleSotivity(sotivity);
		}
	}

	public void updateSotivityModel() {
		sotivities = businessService.getSotivities();
		sotivityModel = new SotivityDataModel(sotivities);
	}

	public List<Sotivity> getFilteredSotivities() {  
        return filteredSotivities;  
    }  
  
    public void setFilteredSotivities(List<Sotivity> filteredSotivities) {  
        this.filteredSotivities = filteredSotivities;  
    }  
    
    public void onSotivityEdit(RowEditEvent event) {
		Sotivity selectedSotivity = ((Sotivity) event.getObject());
		boolean isDone = ((Sotivity) event.getObject()).isDone();		
		setSingleIsDone(selectedSotivity, isDone);
		changedTrustStatusMessage(selectedUser, isDone);
	}

	public void onSotivityCancel(RowEditEvent event) {
//		FacesMessage msg = new FacesMessage("Benutzer Abgebrochen",
//				(((User) event.getObject()).getId()).toString());
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
    
	public void setSingleIsDone(Sotivity selectedSotivity, boolean isDone){
		selectedSotivity.setDone(isDone);
		boolean success = businessService.updateSotivity(selectedSotivity);
		
		logger.debug("sot: "+selectedSotivity.getTitle()+" set to "+selectedSotivity.isDone() + "boolean");
		updateSotivityModel();
	}
	
	public void setMultipleIsDone(Sotivity[] selectedSotivity, boolean isDone){
		for(Sotivity sotivity:selectedSotivities){
			setSingleIsDone(sotivity, isDone);
		}
	}
	
	public void setSotivityDone(boolean isDone) {
		if (this.selectedSotivity != null) {
			setSingleIsDone(this.selectedSotivity, isDone);
		} else if (this.selectedSotivities.length > 0) {
			setMultipleIsDone(this.selectedSotivities, isDone);
		}

		this.updateSotivityModel();
	}
	
	
	/*
	 * * USERS
	 */

	public User[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(User[] selectedUsers) {
		logger.debug("selected users size:" + selectedUsers.length);
		this.selectedUsers = selectedUsers;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public UserDataModel getUserModel() {
		return userModel;
	}

	public void trustUsers(boolean isTrusted) {
		if (this.selectedUser != null) {
			logger.debug("in selected user - is trusted: " + isTrusted);
			trustOrUntrustUser(this.selectedUser, isTrusted);
			changedTrustStatusMessage(this.selectedUser, isTrusted);
		} else if (this.selectedUsers.length > 0) {
			trustOrUntrustUsers(this.selectedUsers, isTrusted);
			logger.debug("in selected userS - is trusted:" + isTrusted);
			changedTrustStatusMessage(null, isTrusted);
		}

		this.updateUserModel();
	}
		

	public void trustOrUntrustUser(User selectedUser, boolean isTrusted) {
		selectedUser.setTrusted(isTrusted);
		businessService.updateUser(selectedUser);
	}

	public void trustOrUntrustUsers(User[] selectedUsers, boolean isTrusted) {
		for (User user : selectedUsers) {
			logger.debug("set users (" + user.getNickname() + ")trust to: "
					+ user.isTrusted());
			if (user.isTrusted() != isTrusted) {
				trustOrUntrustUser(user, isTrusted);
			}
		}
	}

	public void deleteUser() {
		if (this.selectedUser != null) {
			deleteSingleUser(this.selectedUser);
		} else if (this.selectedUsers.length > 0) {
			deleteMultipleUsers(this.selectedUsers);
		}

		this.updateUserModel();
	}

	public void deleteSingleUser(User user) {
		logger.debug("deleting user with name " + user.getNickname());
		businessService.deleteUser(user);
	}

	public void deleteMultipleUsers(User[] users) {
		for (User user : users) {
			deleteSingleUser(user);
		}
	}

	public void updateUserModel() {
		users = businessService.getAllUsers();
		userModel = new UserDataModel(users);
	}

	public void onEdit(RowEditEvent event) {
		User selectedUser = ((User) event.getObject());
		boolean isTrusted = ((User) event.getObject()).isTrusted();		
		trustOrUntrustUser(selectedUser, isTrusted);
		changedTrustStatusMessage(selectedUser, isTrusted);
	}

	public void onCancel(RowEditEvent event) {
//		FacesMessage msg = new FacesMessage("Benutzer Abgebrochen",
//				(((User) event.getObject()).getId()).toString());
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void changedTrustStatusMessage(User selectedUser, boolean isTrusted){
		String infoMsg = "";

		if (selectedUser != null) {
			infoMsg = "Vertrauenswürdigkeit von "
					+ selectedUser.getNickname() + " erfolgreich auf "
					+ isTrusted
					+ " gesetzt";
		} else {
			infoMsg = "Vertrauenswürdigkeit der markierten Benutzer wurde erfolgreich auf "
					+ isTrusted
					+ " gesetzt";
		}

		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, infoMsg, ""));
	}
	
	public List<User> getFilteredUsers() {  
        return filteredUsers;  
    }  
  
    public void setFilteredUsers(List<User> filteredUsers) {  
        this.filteredUsers = filteredUsers;  
    }  
    
    private SelectItem[] createFilterOptions(String[] data)  {  
        SelectItem[] options = new SelectItem[data.length + 1];  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < data.length; i++) {  
            options[i + 1] = new SelectItem(data[i], data[i]);  
        }  
  
        return options;  
    }  
}
