package com.wday.search.api.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainApp {
	
	private static Properties retrieveProps = new Properties();
	
	public static void main(String[] args) {
		
		if (args.length == 0) {
			// No Configuration file is provided
			System.err.println("No input file is given.");
			System.exit(-1);
		}
	
		InputStream settingFileStream = MainApp.class.getResourceAsStream("app-settings.properties");
		 retrieveProps = System.getProperties();
		try {
			retrieveProps.load(settingFileStream);
		} catch (IOException exception) {
			System.exit(-1);
		}
		
		SearchAPIManager.manageAPI();
	}

}
