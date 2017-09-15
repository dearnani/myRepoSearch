package com.wday.search.api.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wday.search.api.util.PropertyServiceLocator;

public class MainApp {
	
	private static Log logger = LogFactory.getLog(MainApp.class);
	
	private static Properties appSettings = new Properties();
	
	public static void main(String[] args) {
		
		appSettings = System.getProperties();
		String fileName = System.getProperty("settingsFile");
		
		if(fileName == null)
			fileName="app-settings.properties";
		System.out.println(String.format("FileName:%s", fileName));
		
		try (InputStream settingFileStream = MainApp.class.getClassLoader().getResourceAsStream(fileName)) {
			appSettings.load(settingFileStream);
			PropertyServiceLocator.getInstance().setProperties(appSettings);
		} catch (IOException exception) {
			logger.error("IOException Occured while setting up the Application properties");
			System.exit(-1);
		}
		
		SearchAPIManager.manageAPI();
	}
}
