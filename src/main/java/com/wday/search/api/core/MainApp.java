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
		
		;
		
		appSettings = System.getProperties();
		String fileName = System.getProperty("settingsFile"); // appSettings.getProperty("settingsFile");
		
		if(fileName == null)
			fileName="app-settings.properties";
		System.out.println(String.format("FileName:%s", fileName));
		try (InputStream settingFileStream = MainApp.class.getClassLoader().getResourceAsStream(fileName)) {
			
			appSettings.load(settingFileStream);
			PropertyServiceLocator.getInstance().setProperties(appSettings);
			
		} catch (IOException exception) {
			System.exit(-1);
		}
		
		SearchAPIManager.manageAPI();
	}
}
