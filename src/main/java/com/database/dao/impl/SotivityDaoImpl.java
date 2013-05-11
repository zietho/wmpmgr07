/**
 * 
 */
package com.database.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.inject.Named;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.constants.Constants;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.dao.SotivityDao;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;

/**
 * @author Patrick
 * 
 */
@Named("sotivityDao")
public class SotivityDaoImpl extends BaseDaoImpl<Sotivity> implements
		SotivityDao {

	public SotivityDaoImpl() {
		super(Sotivity.class);
	}
	
	public List<Sotivity> getSotivitiesAroundLocation(double lat, double lon,
			double squareInKM) {
		// only suitable for Vienna

		Session session = this.getSessionFactory().openSession();

		double deltaLon = Constants.lonViennaQuadrant * squareInKM; // 70 only Vienna
		double deltaLat = Constants.latViennaQuadrant * squareInKM; // 111 constant

		String query = "from Sotivity s where s.address.latitude >= :latMin and s.address.latitude <= :latMax"
				+ " and s.address.longitude >= :lonMin and s.address.longitude <= :lonMax";

		@SuppressWarnings("unchecked")
		List<Sotivity> sotivities = session.createQuery(query)
				.setDouble("latMin", lat - deltaLat)
				.setDouble("latMax", lat + deltaLat)
				.setDouble("lonMin", lon - deltaLon)
				.setDouble("lonMax", lon + deltaLon).list();

		session.close();

		return sotivities;
	}

	@Override
	public List<Sotivity> getAllUserSotivities(User user, Integer done) {
		Session session = this.getSessionFactory().openSession();

		//String query = "from Sotivity s where s.user.id = :userid or exists (s.workflow w where)";
		String query = "from Sotivity s where (s.user.id = :userid or exists (from Workflow w where s.id = w.sotivity.id and w.user.id = :userid))";
		if(done != null)
			query += " and s.done="+done.toString();
		
		@SuppressWarnings("unchecked")
		List<Sotivity> sotivities = session.createQuery(query)
				.setDouble("userid", user.getId())
				.list();

		session.close();

		return sotivities;
	}
	
	@Override
	public List<Sotivity> getOwnedUserSotivities(User user, Integer done) {
		Session session = this.getSessionFactory().openSession();

		String query = "from Sotivity s where s.user.id = :userid";
		if(done != null)
			query += " and s.done="+done.toString();
		
		@SuppressWarnings("unchecked")
		List<Sotivity> sotivities = session.createQuery(query)
				.setDouble("userid", user.getId())
				.list();

		session.close();

		return sotivities;
	}
	
	@Override
	public List<Sotivity> getOfferUserSotivities(User user, Integer done) {
		Session session = this.getSessionFactory().openSession();

		String query = "from Sotivity s where exists (from Workflow w where s.id = w.sotivity.id and w.user.id = :userid)";
		if(done != null)
			query += " and s.done="+done.toString();
		
		@SuppressWarnings("unchecked")
		List<Sotivity> sotivities = session.createQuery(query)
				.setDouble("userid", user.getId())
				.list();

		session.close();

		return sotivities;
	}
	
	/**
	 * Baut aufgrund der übergebenen Parameter eine konkatenierte Suche zusammen
	 */
	public List<Sotivity> searchUltimate(Double lat, Double lon, Double squareInKM,Category category,Skill skill,Date dateEnd,Integer maxDuration,String queryText) {
		Session session = this.getSessionFactory().openSession();
		String query = "select distinct s from Sotivity s left join s.sotivityCategories c left join s.sotivitySkills as k where s.done=0 ";

		if(queryText!=null && "".equals(queryText)){
			queryText=null;
		}
		
		if(queryText!=null){
			queryText=queryText.replace("*", "%");
		}
		
		
		double deltaLon=0;
		double deltaLat=0;
		
		if(category !=null){
			query+="and  c.id = :cat_id";
		}
		if(skill !=null){
			query+="and  k.id = :skill_id";
		}
		if(lat!=null && lon!=null && squareInKM!=null){
		
			deltaLon = Constants.lonViennaQuadrant * squareInKM; // 70 only Vienna
			deltaLat = Constants.latViennaQuadrant * squareInKM; // 111 constant
			
			query+="and s.address.latitude >= :latMin and s.address.latitude <= :latMax" + " and s.address.longitude >= :lonMin and s.address.longitude <= :lonMax";
		}
		if(dateEnd!=null){
			query+="and s.dateEnd <= :datEnd";
		}
		if(maxDuration!=null){
			query+="and s.duration <= :maxDuration";
		}
		if(queryText!=null && !"".equals(queryText)){
			query+="and lower(s.title) like :title or lower(s.description) like :description or s.id= :id";
		}
		
		Query queryQ=session.createQuery(query);
		List<Sotivity> sotivities = null;
		if(category!=null){			
			queryQ.setInteger("cat_id", category.getId());
		}
		if(skill!=null){			
			queryQ.setInteger("skill_id", skill.getId());
		}
		if(lat!=null && lon!=null && squareInKM!=null){
			queryQ.setDouble("latMin", lat - deltaLat)
			.setDouble("latMax", lat + deltaLat)
			.setDouble("lonMin", lon - deltaLon)
			.setDouble("lonMax", lon + deltaLon);
		}
		if(dateEnd!=null){
			queryQ.setDate("datEnd", dateEnd);
		}
		if(maxDuration!=null){
			queryQ.setInteger("maxDuration", maxDuration);
		}
		if(queryText!=null && !"".equals(queryText)){
			queryQ.setString("title", "%"+queryText.toLowerCase()+"%")
			.setString("description", "%"+queryText.toLowerCase()+"%");
			
			Integer ref=null;
			try{
				ref=Integer.parseInt(queryText);
			}catch(Throwable t){
				ref=-1;
			}
			queryQ.setInteger("id",ref);
			
		}
		
		sotivities=queryQ.list();
		
		session.close();
		
		List<Sotivity> filteredSotivities=new Vector<Sotivity>();
		
		//Überlagerung Quadrat und Kreis filtern
		if(lat!=null && lon!=null && squareInKM!=null){
			MyGeoPoint gp=new MyGeoPoint(lat, lon);
			double squareInMeter=squareInKM*1000;
			for(Sotivity sot : sotivities){
				if(sot.getAddress()!=null){
					MyGeoPoint sgp=new MyGeoPoint(sot.getAddress().getLatitude(), sot.getAddress().getLongitude());
					if(LocationAPI.getDistanceInMeter(gp, sgp)<=squareInMeter)
						filteredSotivities.add(sot);
				}
			}
			
			return filteredSotivities;
		}
		
		return sotivities;
	}

}
