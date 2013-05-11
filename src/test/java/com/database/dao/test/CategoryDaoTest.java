 package com.database.dao.test;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import com.database.bean.Address;
import com.database.bean.BaseBean;
import com.database.bean.Category;
import com.database.dao.AddressDao;
import com.database.dao.BaseDao;

public class CategoryDaoTest extends AbstractBaseDaoTests {
	@Inject
	AddressDao addressDao;

	@Override
	public BaseDao<? extends BaseBean> getDao() {
		return addressDao;
	}

	@Override
	public BaseBean getSampleBean() {
		// TODO Auto-generated method stub
		return new Address();
	}
	


}
