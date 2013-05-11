package com.database.dao.impl;

import javax.inject.Named;

import com.database.bean.Category;
import com.database.dao.CategoryDao;

@Named("categoryDao")
public class CategoryDaoImpl extends BaseDaoImpl<Category> implements CategoryDao {


	public CategoryDaoImpl() {
		super(Category.class);
	}

}
