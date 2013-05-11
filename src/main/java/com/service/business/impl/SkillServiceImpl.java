package com.service.business.impl;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.database.bean.Skill;
import com.database.dao.SkillDao;
import com.service.business.SkillService;

@Component
public class SkillServiceImpl implements Serializable, SkillService{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private SkillDao skillDao;
	
	public List<Skill> getAllSkills(){
		return skillDao.getAll();
	}

}
