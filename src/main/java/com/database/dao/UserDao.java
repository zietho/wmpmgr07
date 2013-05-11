package com.database.dao;

import com.database.bean.User;

/**
 * 
 * @author Patrick
 *
 */
public interface UserDao extends BaseDao<User> {

	public User getUserByNickname(String nickname);
}
