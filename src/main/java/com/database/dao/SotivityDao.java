package com.database.dao;

import java.util.Date;
import java.util.List;

import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;

/**
 * 
 * @author Patrick, Chris
 *
 */
public interface SotivityDao extends BaseDao<Sotivity> {
	public List<Sotivity> getSotivitiesAroundLocation(double latitude, double longitude, double squareInKM);
	public List<Sotivity> getAllUserSotivities(User user, Integer done);
	public List<Sotivity> getOwnedUserSotivities(User user, Integer done);
	public List<Sotivity> getOfferUserSotivities(User user, Integer done);
	public List<Sotivity> searchUltimate(Double lat, Double lon, Double squareInKM,Category category,Skill skill,Date dateEnd,Integer maxDuration,String queryText);
}
