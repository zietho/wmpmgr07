package com.database.dao.impl;

import javax.inject.Named;

import com.database.bean.Skill;
import com.database.dao.SkillDao;

@Named("skillDao")
public class SkillDaoImpl extends BaseDaoImpl<Skill> implements SkillDao {


	public SkillDaoImpl() {
		super(Skill.class);
	}

}