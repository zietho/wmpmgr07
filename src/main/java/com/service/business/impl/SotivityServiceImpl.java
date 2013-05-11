package com.service.business.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.bean.Workflow;
import com.database.dao.SotivityDao;
import com.database.dao.WorkflowDao;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.server.util.ConfigurationHelper;
import com.service.business.SotivityService;
import com.util.email.Email;

@Component
public class SotivityServiceImpl implements Serializable, SotivityService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private SotivityDao sotivityDao;
	@Inject
	private WorkflowDao workflowDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public Sotivity createSotivity(User user, boolean publicVisible,
			boolean groupSotivity, String title, String description,
			Timestamp dateEnd) {
		Sotivity sotivity = new Sotivity();
		sotivity.setUser(user);
		sotivity.setDateEnd(dateEnd);
		sotivity.setTitle(title);
		sotivity.setDescription(description);
		sotivity.setPublicVisible(publicVisible);
		sotivity.setGroupSotivity(groupSotivity);

		sotivityDao.create(sotivity);
		return sotivity;
	}

	public int createSotivity(Sotivity sotivity) {
		return sotivityDao.create(sotivity);
	}

	public boolean deleteSotivity(User user, Sotivity sotivity) {
		sotivityDao.delete(sotivity);
		return true;
	}

	public boolean updateSotivity(Sotivity sotivity) {
		sotivityDao.update(sotivity);
		return true;
	}

	public boolean updateWorkflow(Workflow workflow) {
		workflowDao.update(workflow);
		return true;
	}

	public List<Sotivity> getSotivities() {
		return sotivityDao.getAll();
	}

	public void sendOffer(User user, Sotivity sotivity, String comment) {
		comment = comment.replaceAll("<.*>", "").trim();
		
		Workflow workflow = new Workflow();
		workflow.setUser(user);
		workflow.setSotivity(sotivity);
		workflow.setComment(comment);
		workflow.setState("OFFER");

		workflowDao.create(workflow);
		
		String sendmail = ConfigurationHelper.getValue("sendmail");
		if( sendmail != null && sendmail.equals("true") ) {
			if(sotivity.getUser().getEmail() == null || sotivity.getUser().getEmail().equals(""))
				return;
			Email email = new Email("Angebot zu '"+sotivity.getTitle()+"' erhalten", "<p>Hallo "+sotivity.getUser().getFirstname()+",</p><p>Du hast gerade ein Angebot zu deiner Sotivity <strong>"+sotivity.getTitle()+"</strong> mit der Referenznummer "+sotivity.getId()+" erhalten:</p><p><i><strong>"+user.getFirstname()+" "+user.getLastname()+" ("+user.getNickname()+")</strong><br />"+comment+"</i></p><p>Bitte geh auf <a href='www.wienerhelden.at'>WienerHelden.at</a> um das Angebot anzunehmen, damit sich der Helfer mit dir in Verbindung setzen kann.<br />Dein WienerHelden Team</p>", "dev@wienerhelden.at", sotivity.getUser().getEmail());
			email.send();
		}

	}
	
	public void editOffer(User user, Sotivity sotivity, String comment) {
		comment = comment.replaceAll("<.*>", "").trim();
		Workflow workflow = null;
		
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(user.getId()))
				workflow = w;
		}
		
		if(workflow == null)
			return;
		
		workflow.setComment(comment);
		workflowDao.update(workflow);
	}
	
	public void withdrawOffer(User user, Sotivity sotivity) {
		Workflow workflow = null;
		
		for(Workflow w : sotivity.getWorkflows()) {
			if(w.getUser().getId().equals(user.getId()))
				workflow = w;
		}
		
		if(workflow == null)
			return;
		
		workflow.setState("WITHDRAWN"); //TODO: NOT USED YET
		workflowDao.update(workflow);
	}

	@Override
	public List<Sotivity> getAllUserSotivities(User user, Integer done) {
		if (user != null) {
			List<Sotivity> ownSotivities = sotivityDao
					.getAllUserSotivities(user, done);
			return ownSotivities;
		}

		return null;
	}

	@Override
	public List<Sotivity> getOwnedUserSotivities(User user, Integer done) {
		if (user != null) {
			List<Sotivity> ownSotivities = sotivityDao
					.getOwnedUserSotivities(user, done);
			return ownSotivities;
		}
		
		return null;
	}

	@Override
	public List<Sotivity> getOfferUserSotivities(User user, Integer done) {
		if (user != null) {
			List<Sotivity> ownSotivities = sotivityDao
					.getOfferUserSotivities(user, done);
			return ownSotivities;
		}

		return null;
	}

	@Override
	public List<Sotivity> searchSotivitiesUltimate(Double lat, Double lon,
			Double squareInKM, Category category, Skill skill, Date dateEnd,
			Integer maxDuration, String queryText) {
		return sotivityDao.searchUltimate(lat, lon, squareInKM, category,
				skill, dateEnd, maxDuration, queryText);
	}

	@Override
	public String publishToFacebook(Sotivity sotivity) {
		FacebookClient facebookClient = new DefaultFacebookClient(
				"AAAEeZA2Khx3wBAFz1PIiLY2pG0Vqf2TjogQ6MOEDbkKGypBknoP9ioWZC4mOql244w9APjkvFSTl5cTWYtfTBRD1P4E4rQ8dtEeRYFAQqyKN6g5Y0U");

		Date startdate = sotivity.getDateCreated();
		DateTimeFormatter dft = ISODateTimeFormat.dateTime();
		Date enddate = sotivity.getDateEnd();
		logger.debug(dft.print(startdate.getTime()));
		FacebookType publishEventResponse = facebookClient.publish(
				"WienerHelden/events", FacebookType.class,
				Parameter.with("name", sotivity.getTitle()),
				Parameter.with("start_time", dft.print(startdate.getTime())),
				Parameter.with("end_time", dft.print(enddate.getTime())),
				Parameter.with("description", sotivity.getDescription()));
		String id = publishEventResponse.getId();
		logger.debug(id);
		sotivity.setFacebookId(id);
		this.updateSotivity(sotivity);
		return id;
	}
	
	public Sotivity getSotivityById(int id){
		return sotivityDao.getById(id);		
	}
}
