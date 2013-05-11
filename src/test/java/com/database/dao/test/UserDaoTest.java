package com.database.dao.test;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.database.bean.BaseBean;
import com.database.bean.User;
import com.database.dao.BaseDao;
import com.database.dao.UserDao;

public class UserDaoTest extends AbstractBaseDaoTests{
	@Inject
	private UserDao userDaoImpl;

    @Before
    public void after() {
        for (BaseBean b :getDao().getAll())
            if (b instanceof User && ((User)b).getNickname().equals("franz"))
                getDao().delete(b);
    }
	
	
	@Test
	public void loginTest() {
		assertNotNull(userDaoImpl.getUserByNickname("matthias"));
	}


	@Override
	public BaseDao<? extends BaseBean> getDao() {
		return userDaoImpl;
	}


	@Override
	public BaseBean getSampleBean() {
		User user = new User();
		user.setEmail("sample@invalid.com");
		user.setNickname("franz");
		user.setPassword("void");
		user.setSalt("void");
		return user;
	}
}
