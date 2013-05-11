package com.web.managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.database.bean.Category;
import com.database.bean.Sotivity;
import com.database.bean.Thanks;
import com.database.bean.User;
import com.database.bean.Workflow;
import com.server.util.ConfigurationHelper;
import com.util.email.Email;
import com.web.util.WebUtils;

@ManagedBean(name = "userSotivitiesMBean")
@SessionScoped
@Controller
public class UserSotivitiesMBean extends AbstractManagedBean {

	private static final long serialVersionUID = 1L;
	
	public static int SotivityTypeAll = 0;
	public static int SotivityTypeOwned = 1;
	public static int SotivityTypeOffer = 2;
	
	private User user;
	private Sotivity selectedSotivity;
	private Integer selectedSotivityType = SotivityTypeAll;

	private String comment;
	
	private Integer selectedSotiState;
	private String offerComment;

	public UserSotivitiesMBean() {
		super();
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

	public void setUser(User user) {
		this.user = user;
	}

	public List<Sotivity> getUserSotivities() {
		List<Sotivity> userSotivities = null;
		
		Integer done = null;
		if(selectedSotiState != null) {
			if(selectedSotiState == 1)
				done = 0;
			else if(selectedSotiState == 2)
				done = 1;
		}
		
		
			
		if (selectedSotivityType == SotivityTypeOwned)
			userSotivities = businessService.getOwnedUserSotivities(this.getUser(), done);
		else if (selectedSotivityType == SotivityTypeOffer)
			userSotivities = businessService.getOfferUserSotivities(this.getUser(), done);
		else
			userSotivities = businessService.getAllUserSotivities(this.getUser(), done);
		
		return userSotivities;
	}
	
	public String getDauer(Sotivity soti) {
		return WebUtils.formattedTimeFromMinutes1(soti.getDuration());
	}

	public Sotivity getSelectedSotivity() {
		return selectedSotivity;
	}

	public void setSelectedSotivity(Sotivity selectedSotivity) {
		this.selectedSotivity = selectedSotivity;
	}

	public String getKategorieName(Sotivity sotivity) {
		for (Category k : sotivity.getSotivityCategories()) {
			return k.getTitle();
		}

		return "Keine Kategorie";
	}
	
	public List<Workflow> getSelectedSotivityAngebote() {
		if (selectedSotivity == null)
			return null;
		return new ArrayList<Workflow>(selectedSotivity.getWorkflows());
	}
	
	public Workflow getSotivityAngebot(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return null;
		
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(helfer.getId()))
				return w;
		}
		
		return null;
	}
	
	public boolean showRejectedMessage(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return false;
		
		//Eigene Sotivity
		if(sotivity.getUser().getId().equals(helfer.getId()))
			return false;
		
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(helfer.getId()))
				if(w.getState().equals("REJECTED"))
					return true;
				else
					return false;
		}
		
		return false;
	}
	
	public boolean showAcceptedMessage(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return false;
		
		//Eigene Sotivity
		if(sotivity.getUser().getId().equals(helfer.getId()))
			return false;
		
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(helfer.getId()))
				if(w.getState().equals("ACCEPTED"))
					return true;
				else
					return false;
		}
		
		return false;
	}
	
	public boolean showChangeOffer(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return false;
		
		//Not done
		if(sotivity.isDone())
			return false;
		
		//Not rejected or accepted
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(helfer.getId()))
				if(w.getState().equals("REJECTED") || w.getState().equals("ACCEPTED"))
					return false;
				else
					break;
		}
		
		return true;
	}
	
	public boolean showThanksMessage(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return false;
		
		//Eigene Sotivity
		if(sotivity.getUser().getId().equals(helfer.getId()))
			return false;
		
		//Noch nicht erledigt
		if(sotivity.isDone() == false)
			return false;
		
		//Erledigt, suche thx message
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(helfer.getId()))
				if(w.getState().equals("ACCEPTED"))
					return true;
				else
					return false;
		}
		
		return false;
	}
	
	public Thanks getThanks(Sotivity sotivity, User helfer) {
		if (sotivity == null || helfer == null)
			return null;
		
		//Eigene Sotivity
		if(sotivity.getUser().getId().equals(helfer.getId()))
			return null;
		
		//Noch nicht erledigt
		if(sotivity.isDone() == false)
			return null;
		
		//Erledigt, suche thx message
		for(Thanks t : sotivity.getThanks()) {
			if(t.getUser().getId().equals(helfer.getId()))
				return t;
		}
		
		return null;
	}
	
	public void acceptOffer(Workflow workflow) {
		if (workflow == null)
			return;

		logger.info("accept " + workflow.getUser().getFirstname());

		if (workflow.getSotivity().isDone()) {
			logger.info("sotivity already done, cannot change state");
			return;
		}

		//if (workflow.getState().equals("ACCEPTED"))
		//	workflow.setState("OFFER");
		//else
		workflow.setState("ACCEPTED");

		businessService.updateWorkflow(workflow);
		
		String sendmail = ConfigurationHelper.getValue("sendmail");
		if( sendmail != null && sendmail.equals("true") ) {
			if(workflow.getUser().getEmail() == null || workflow.getUser().getEmail().equals(""))
				return;
			Email email = new Email("Angebot zu '"+workflow.getSotivity().getTitle()+"' akzeptiert", "<p>Hallo "+workflow.getUser().getFirstname()+",</p><p>Dein Angebot bei der Sotivity <strong>"+workflow.getSotivity().getTitle()+"</strong> mit der Referenznummer "+workflow.getSotivity().getId()+" zu helfen wurde von <strong>"+user.getFirstname()+" "+user.getLastname()+"</strong> angenommen.</p><p>Bitte geh auf <a href='www.wienerhelden.at'>WienerHelden.at</a> um dich mit dem Hilfesuchenden in Verbindung zu setzen.<br />Dein WienerHelden Team</p>", "dev@wienerhelden.at", workflow.getUser().getEmail());
			email.send();
		}
	}

	public int getCategoryId(Sotivity sotivity) {
		for (Category category : sotivity.getSotivityCategories())
			return category.getId();
		return 1;
	}

	public boolean isAbschliessbar(Sotivity sotivity) {
		if (sotivity.isDone())
			return false;

		for (Workflow w : sotivity.getWorkflows()) {
			if (w.getState().equals("ACCEPTED"))
				return true;
		}

		return false;
	}

	public void abschliessen() {
		logger.info("close " + selectedSotivity.getTitle());

		selectedSotivity.setDone(true);
		businessService.updateSotivity(selectedSotivity);
		
		this.comment = comment.replaceAll("<.*>", "").trim();

		for (Workflow wf : this.selectedSotivity.getWorkflows()) {
			if ("ACCEPTED".equals(wf.getState())) {
				// neuer Thank
				Thanks thx = new Thanks();
				thx.setComment(this.comment);
				// thx.setDateCreated(dateCreated);
				thx.setSotivity(this.selectedSotivity);
				thx.setUser(wf.getUser());
				this.businessService.createThanks(thx);
				
				
				String sendmail = ConfigurationHelper.getValue("sendmail");
				if( sendmail != null && sendmail.equals("true") ) {
					if(wf.getUser().getEmail() == null || wf.getUser().getEmail().equals(""))
						return;
					Email email = new Email("Danke zu '"+selectedSotivity.getTitle()+"' erhalten", "<p>Hallo "+wf.getUser().getFirstname()+",</p><p>Du hast gerade ein Dankeschön zur Sotivity <strong>"+selectedSotivity.getTitle()+"</strong> mit der Referenznummer "+selectedSotivity.getId()+" erhalten:</p><p><i><strong>"+selectedSotivity.getUser().getFirstname()+" "+selectedSotivity.getUser().getLastname()+" ("+selectedSotivity.getUser().getNickname()+")</strong><br />"+this.comment+"</i></p><p>Danke auch von <a href='www.wienerhelden.at'>WienerHelden.at</a>. Machen wir Wien zu einem besseren Ort.<br />Dein WienerHelden Team</p>", "dev@wienerhelden.at", wf.getUser().getEmail());
					email.send();
				}
			}
		}
		
		this.comment = "";
	}
	
	public String getThanks(Workflow workflow) {
		for(Thanks t : workflow.getSotivity().getThanks()) {
			if(t.getUser().getId().equals(workflow.getUser().getId()))
				return t.getComment();
		}
		return "";
	}

	public void rejectOffer(Workflow workflow) {
		if (workflow == null)
			return;

		logger.info("reject " + workflow.getUser().getFirstname());

		if (workflow.getSotivity().isDone()) {
			logger.info("sotivity already done, cannot change state");
			return;
		}

		if (workflow.getState().equals("REJECTED"))
			workflow.setState("OFFER");
		else
			workflow.setState("REJECTED");

		businessService.updateWorkflow(workflow);
	}

	public String edit() {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpSession httpSession = request.getSession(false);
		httpSession.removeAttribute("sotivityMBean");
		httpSession.setAttribute("sotivity", this.getSelectedSotivity());

		return "sotivity?faces-redirect=true";

	}
	
	public void editOffer() {
		businessService.editOffer(getUser(), selectedSotivity, offerComment);

		this.offerComment = "";
	}
	
	public void withdrawOffer() {
		businessService.withdrawOffer(getUser(), selectedSotivity);
		
		this.offerComment = "";
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void postOnFacebook() {
		String id = null;
		try {
		id = businessService.publishToFacebook(getSelectedSotivity());
		
		} catch (com.restfb.exception.FacebookOAuthException e ) {
		}
		if (id != null) {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO,"Facebook","Sotivity wurde auf Facebook veröffentlicht..."));
		} else {
			addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Facebook",
					"Sotivity konnte nicht auf Facebook veröffentlicht werden. Bitte probieren Sie es später erneut..."));
		}
	}

	public Integer getSelectedSotivityType() {
		return selectedSotivityType;
	}

	public void setSelectedSotivityType(Integer selectedSotivityType) {
		this.selectedSotivityType = selectedSotivityType;
	}

	public Integer getSelectedSotiState() {
		return selectedSotiState;
	}

	public void setSelectedSotiState(Integer selectedSotiState) {
		this.selectedSotiState = selectedSotiState;
	}

	public String getOfferComment() {
		return offerComment;
	}

	public void setOfferComment(String offerComment) {
		this.offerComment = offerComment;
	}
}
