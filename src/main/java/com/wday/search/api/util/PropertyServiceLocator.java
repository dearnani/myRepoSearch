package com.wday.search.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * Utility Class to read the properties from configuration Files.
 * We can write the properties, reads all properties, individual property and caches  
 * 
 * @author Narasimha
 *
 */
public class PropertyServiceLocator {
	
	private static Log logger = LogFactory.getLog(PropertyServiceLocator.class);
	
	private final Properties configProp = new Properties();

	private PropertyServiceLocator() {
		// Private constructor to restrict new instances
		logger.debug("Read all properties from properties file: application.properties");
		try (InputStream configInputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");){
			configProp.load(configInputStream);
		} catch (IOException e) {
			logger.error(String.format("IOException Occured while reading the properties from application.properties", e.getMessage()));
		}
	}

	// singleton pattern
	private static class LazyHolder {
		private static final PropertyServiceLocator INSTANCE = new PropertyServiceLocator();
	}

	public static PropertyServiceLocator getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return configProp.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return configProp.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return configProp.containsKey(key);
	}
	
	public Properties getProperties() {
		return configProp;
	}
	
	public void setProperties(Properties settingProps) {
		 configProp.putAll(settingProps);
	}
}