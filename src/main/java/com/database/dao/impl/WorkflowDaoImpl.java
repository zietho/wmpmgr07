package com.database.dao.impl;

import javax.inject.Named;

import com.database.bean.Workflow;
import com.database.dao.WorkflowDao;

@Named("workflowDao")
public class WorkflowDaoImpl extends BaseDaoImpl<Workflow> implements WorkflowDao {

	public WorkflowDaoImpl() {
		super(Workflow.class);
	}

}
