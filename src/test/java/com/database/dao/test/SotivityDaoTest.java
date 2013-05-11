package com.database.dao.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.constants.Constants;
import com.database.bean.BaseBean;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.dao.BaseDao;
import com.database.dao.SotivityDao;
import com.util.geolocation.LocationAPI;
import com.util.geolocation.MyGeoPoint;

public class SotivityDaoTest extends AbstractBaseDaoTests {
	@Inject
	SotivityDao sotivityDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public BaseBean getSampleBean() {
		BaseBean sampleBean = new Sotivity();
		Sotivity sot = (Sotivity) sampleBean;
		sot.setDescription("Hello World");
		sot.setGroupSotivity(true);
		sot.setTitle("H1");
		return sampleBean;
	}

	@Override
	public BaseDao<? extends BaseBean> getDao() {
		return sotivityDao;
	}
	
	@Test
	public void getSotivitiesAroundLocationTest() {
		List<Sotivity> sotivities = sotivityDao.getSotivitiesAroundLocation(48.19898,16.36990,0.1);
		
		for(Sotivity s : sotivities) {
			logger.debug(s.getTitle());
		}
	}
	
	@Test
	public void searchSotivityUltimateTest() {
		
		double km=10d;
		String queryText=null; 
		
		List<Sotivity> sotivities = sotivityDao.searchUltimate(Constants.VIENNA_CENTRE.getLatitude(), Constants.VIENNA_CENTRE.getLongitude(), km, null,null,null, null, queryText);
		
		int i=0;
		for(Sotivity sot:sotivities){
			i++;
			MyGeoPoint gp=new MyGeoPoint(sot.getAddress().getLatitude(),sot.getAddress().getLongitude());
			logger.debug(i+". --> d="+LocationAPI.getDistanceInMeter(Constants.VIENNA_CENTRE,gp));
			logger.debug("title="+sot.getTitle()+" \ndescr="+sot.getDescription());
			logger.debug("cat="+sot.getSotivityCategories().toArray());
			logger.debug("---------------------------------------------------------------------");
		}
		
		assertFalse(sotivities.isEmpty());
	}
	
	@Test
	public void getOwnedUserSotivities() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getOwnedUserSotivities(user, null).isEmpty());
	}
	
	@Test
	public void getOwnedUserSotivitiesDone() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getOwnedUserSotivities(user, 1).isEmpty());
	}
	
	@Test
	public void getOfferUserSotivities() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getOfferUserSotivities(user, null).isEmpty());
	}
	
	@Test
	public void getOfferUserSotivitiesDone() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getOfferUserSotivities(user, 1).isEmpty());
	}
	
	@Test
	public void allUserSotivities() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getAllUserSotivities(user, null).isEmpty());
	}
	
	@Test
	public void allUserSotivitiesDone() {
		User user = new User();
		user.setId(-5);
		assertTrue(sotivityDao.getAllUserSotivities(user, 1).isEmpty());
	}
}
