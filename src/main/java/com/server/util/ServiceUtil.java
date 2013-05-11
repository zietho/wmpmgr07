package com.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.service.business.BusinessService;

public class ServiceUtil {
	private static ServiceUtil instance;

	private BusinessService businessService;

	private static Logger logger = LoggerFactory.getLogger(ServiceUtil.class);

	public static ServiceUtil getInstance() {
		if (instance == null) {
			logger.warn("Instance is null");
			instance = new ServiceUtil();
		}
		return instance;
	}

	public void setService(BusinessService businessService) {
		this.businessService = businessService;
	}

	public BusinessService getService() {
		logger.debug("get service {}", businessService);
		return businessService;
	}
}
