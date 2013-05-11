package com.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationHelper {
	private static Properties properties = null;
	private static Logger logger = LoggerFactory
			.getLogger(ConfigurationHelper.class);

	public static String getValue(String key) {
		if (properties == null) {
			try {
				properties = new Properties();
				InputStream in = ConfigurationHelper.class.getClassLoader()
						.getResourceAsStream("config.properties");
				properties.load(in);
			} catch (IOException e) {
				logger.warn("IOException while reading properties", e);
				properties = null;
				return null;
			}
		}

		return properties.getProperty(key);
	}

}
