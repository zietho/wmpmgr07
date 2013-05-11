package com.service.business;

import java.util.List;

import com.database.bean.Address;

public interface AddressService {

	public abstract void updateAddress(Address address);

	public abstract void createAddress(Address address);

	public abstract void deleteAddress(Address address);
	
	public List<Address> searchAddresses();

}