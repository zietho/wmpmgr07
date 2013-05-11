package com.database.dao.impl;

import javax.inject.Named;

import com.database.bean.Address;
import com.database.dao.AddressDao;


@Named("addressDao")
public class AddressDaoImpl extends BaseDaoImpl<Address> implements AddressDao {

	public AddressDaoImpl() {
		super(Address.class);
	}

	

}
