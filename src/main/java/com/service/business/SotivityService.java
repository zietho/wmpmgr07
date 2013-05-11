package com.service.business;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.bean.Workflow;

public interface SotivityService {

	public abstract int createSotivity(Sotivity sotivity);

	public abstract boolean deleteSotivity(User user, Sotivity sotivity);

	public abstract boolean updateSotivity(Sotivity sotivity);

	public abstract boolean updateWorkflow(Workflow workflow);
	
	/*
	 * Liefert alle User Sotivities und alle Sotivities, denen Angebote geschickt wurden (Workflow)
	 */
	public abstract List<Sotivity> getAllUserSotivities(User user, Integer done);

	public abstract List<Sotivity> getOwnedUserSotivities(User user, Integer done);
	
	public abstract List<Sotivity> getOfferUserSotivities(User user, Integer done);

	public abstract List<Sotivity> searchSotivitiesUltimate(Double lat, Double lon, Double squareInKM,Category category,Skill skill,Date dateEnd,Integer maxDuration,String queryText);

	public abstract List<Sotivity> getSotivities();

	public abstract void sendOffer(User user, Sotivity sotivity, String comment);
	
	public abstract void editOffer(User user, Sotivity sotivity, String comment);
	
	public abstract void withdrawOffer(User user, Sotivity sotivity);

	public abstract String publishToFacebook(Sotivity sotivity);
	
	public abstract Sotivity getSotivityById(int id);
}