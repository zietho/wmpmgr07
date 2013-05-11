package com.service.business.impl;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.database.bean.Category;
import com.database.dao.CategoryDao;
import com.service.business.CategoryService;

@Component
public class CategoryServiceImpl implements Serializable, CategoryService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private CategoryDao categoryDao;
	
	public List<Category> getAllCategories(){
		return categoryDao.getAll();
	}

}
