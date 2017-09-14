package com.wday.search.api.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.wday.search.api.util.PropertyServiceLocator;

public class MainApp {
	
	private static Properties appSettings = new Properties();
	
	public static void main(String[] args) {
		
//		if (args.length == 0) {
//			// No Configuration file is provided
//			System.err.println("No input file is given.");
//			System.exit(-1);
//		}
		
		System.out.println("SettinggFile:"+System.getProperty("settingsFile"));
		appSettings = System.getProperties();
		try (InputStream settingFileStream = MainApp.class.getClassLoader().getResourceAsStream("app-settings.properties")) {
			
			appSettings.load(settingFileStream);
			PropertyServiceLocator.getInstance().setProperties(appSettings);
			
		} catch (IOException exception) {
			System.exit(-1);
		}
		
		SearchAPIManager.manageAPI();
	}

}
