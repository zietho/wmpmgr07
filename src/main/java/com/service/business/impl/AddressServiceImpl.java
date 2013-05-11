package com.service.business.impl;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.database.bean.Address;
import com.database.bean.Sotivity;
import com.database.bean.User;
import com.database.dao.AddressDao;
import com.service.business.AddressService;

@Service
public class AddressServiceImpl implements AddressService,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private AddressDao addressDao;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void updateAddress(Address address) {
		addressDao.update(address);
	}
	
	@Override
	public void createAddress(Address address) {
		addressDao.create(address);
	}

	@Override
	public void deleteAddress(Address address) {
		addressDao.delete(address);
	}

	
	public List<Address> searchAddresses() {
		return addressDao.getAll();
	}
	
	


}
