package com.database.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.cxf.common.jaxb.JAXBUtils.Mapping;
import org.hibernate.MappingException;
import org.junit.Test;

import at.wienerhelden.BaseUnitTest;

import com.database.bean.BaseBean;
import com.database.bean.User;
import com.database.dao.BaseDao;

public abstract class AbstractBaseDaoTests extends BaseUnitTest {

	public abstract BaseBean getSampleBean();

	public abstract BaseDao<? extends BaseBean> getDao();

	@Test
	public void simpleTestCreate() {
		assertTrue(getDao().create(getSampleBean()) == 0);
		getDao().delete(getSampleBean());
	}

	@Test
	public void createAndGetAllTest() {
		assertTrue(getDao().create(getSampleBean()) == 0);
		assertFalse(getDao().getAll().isEmpty());
		getDao().delete(getSampleBean());
	}

	// @Test
	// public abstract void createAndUpdateTest();

	@Test
	public void getByIdTest() {
        BaseBean b = getSampleBean();
		assertTrue(getDao().create(b) == 0);
		assertEquals(b, getDao().getById(b.getId()));
		getDao().delete(getSampleBean());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNull() {
		assertFalse(getDao().create(null) == 0);
	}

	@Test(expected=MappingException.class)
	public void createInvalidBaseBean() {
		assertFalse(getDao().create(new BaseBean()) == 0);
	}

	@Test
	public void deleteBaseBean() {
		assertTrue(getDao().create(getSampleBean()) == 0);
		assertTrue(getDao().delete(getSampleBean()) == 0);
	}
}
