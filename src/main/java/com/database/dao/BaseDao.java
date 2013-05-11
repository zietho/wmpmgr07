package com.database.dao;

import java.util.List;

import com.database.bean.BaseBean;

public interface BaseDao<B extends BaseBean> {
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	B getById(Integer id);
	

	/**
	 * 
	 * @return
	 */
	public List<B> getAll();
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public int create(BaseBean b);
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public int update(BaseBean b);
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public int delete(BaseBean b);
}
