package com.service.business.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.database.bean.Address;
import com.database.bean.Category;
import com.database.bean.Skill;
import com.database.bean.Sotivity;
import com.database.bean.Thanks;
import com.database.bean.User;
import com.database.bean.Workflow;
import com.database.dao.AddressDao;
import com.server.util.ServiceUtil;
import com.service.business.AddressService;
import com.service.business.BusinessService;
import com.service.business.CategoryService;
import com.service.business.SkillService;
import com.service.business.SotivityService;
import com.service.business.ThanksService;
import com.service.business.UserService;

@Service
@TransactionConfiguration
@Transactional(propagation = Propagation.REQUIRED)
public class BusinessServiceImpl implements BusinessService, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private UserService userService;
	@Autowired
	private SotivityService sotivityService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private SkillService skillService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ThanksService thanksService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public BusinessServiceImpl() {
		logger.debug("Create service");
		ServiceUtil.getInstance().setService(this);
	}

//	@Override
//	@Transactional
	// public Sotivity createSotivity(User user, boolean publicVisible,
	// boolean groupSotivity, String title, String description,
	// Timestamp dateEnd) {
	// return sotivityService.createSotivity(user, publicVisible, groupSotivity,
	// title, description, dateEnd);
	// }
	
	
	@Override
	@Transactional
	public int createSotivity(Sotivity sotivity) {
		return sotivityService.createSotivity(sotivity);
	}

	@Override
	@Transactional
	public boolean deleteSotivity(User user, Sotivity sotivity) {
		return sotivityService.deleteSotivity(user, sotivity);
	}

	@Override
	@Transactional
	public boolean updateSotivity(Sotivity sotivity) {
		return sotivityService.updateSotivity(sotivity);
	}

	@Override
	@Transactional
	public boolean updateWorkflow(Workflow workflow) {
		return sotivityService.updateWorkflow(workflow);
	}

	@Override
	@Transactional
	public boolean createUser(String firstname, String lastname,
			String nickname, String email, String password) {
		return userService.createUser(firstname, lastname, nickname, email,
				password);
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userService.updateUser(user);
	}

	@Override
	@Transactional
	public void sendOffer(User user, Sotivity sotivity, String comment) {
		sotivityService.sendOffer(user, sotivity, comment);
	}
	
	@Override
	@Transactional
	public void editOffer(User user, Sotivity sotivity, String comment) {
		sotivityService.editOffer(user, sotivity, comment);
	}
	
	@Override
	@Transactional
	public void withdrawOffer(User user, Sotivity sotivity) {
		sotivityService.withdrawOffer(user, sotivity);
	}

	@Override
	@Transactional
	public User login(String name, String password) {
		return userService.login(name, password);
	}

	@Override
	@Transactional
	public List<Category> getAllCategories() {
		return categoryService.getAllCategories();
	}
	
	@Override
	public List<Skill> getAllSkills() {
		return skillService.getAllSkills();
	}

	@Override
	@Transactional
	public List<Sotivity> getSotivities() {
		return sotivityService.getSotivities();
	}

	public void updateAddress(Address address) {
		addressService.updateAddress(address);
	}

	@Override
	@Transactional
	public void deleteAddress(Address address) {
		addressService.deleteAddress(address);
	}

	@Override
	@Transactional
	public void createAddress(Address address) {
		logger.info("new address: {}",address);
		addressService.createAddress(address);
	}
	
	@Override
	@Transactional
	public void createThanks(Thanks thanks) {
		logger.info("new thanks: {}",thanks);
		thanksService.createThanks(thanks);
	}

	@Override
	@Transactional
	public boolean changeProfilePicture(User user, UploadedFile file) {
		return userService.changeProfilePicture(user, file);
	}

	
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}

	@Override
	public List<Sotivity> getAllUserSotivities(User user, Integer done) {
		return sotivityService.getAllUserSotivities(user, done);
	}
	
	@Override
	public List<Sotivity> getOwnedUserSotivities(User user, Integer done) {
		return sotivityService.getOwnedUserSotivities(user, done);
	}
	
	@Override
	public List<Sotivity> getOfferUserSotivities(User user, Integer done) {
		return sotivityService.getOfferUserSotivities(user, done);
	}

	@Override
	public List<Sotivity> searchSotivitiesUltimate(Double lat, Double lon,Double squareInKM, Category category,Skill skill, Date dateEnd,Integer maxDuration, String queryText) {
		return sotivityService.searchSotivitiesUltimate(lat, lon, squareInKM, category,skill, dateEnd,maxDuration, queryText);

	}

	@Override
	public String publishToFacebook(Sotivity sotivity) {
		return sotivityService.publishToFacebook(sotivity);
	}
	
	@Override
	public boolean deleteUser(User user){
		return userService.deleteUser(user);
	}

	public Sotivity getSotivityById(int id){
		return sotivityService.getSotivityById(id);
	}

	@Override
	public List<Address> searchAddresses() {
		return addressService.searchAddresses();
	}

	@Override
	public boolean isNicknameUsed(String nickname) {
		return userService.isNicknameUsed(nickname);
	}
	
}
