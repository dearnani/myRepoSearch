package com.wday.search.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppSettingUpdateUtil {

	private static Log logger = LogFactory.getLog(AppSettingUpdateUtil.class);
	private static Properties appSettings = new Properties();
	/**
	 * Utility class to synchronise the application settings, application properties
	 */
	public static void applyApplicationSettings()
	{
		appSettings = System.getProperties();
		// The expected command line argument parameter name , Ex: settingsFile
		String fileName = System.getProperty(PropertyServiceLocator.getInstance().getProperty("config.app.setting.fileParameter"));
		if(fileName == null) // sets the default application settings properties file name, changes this value should be at pom.xml as well.
			fileName=PropertyServiceLocator.getInstance().getProperty("config.app.default.application.fileName");
		try (InputStream settingFileStream = AppSettingUpdateUtil.class.getClassLoader().getResourceAsStream(fileName)) {
			appSettings.load(settingFileStream); // loads app-settings properties
			PropertyServiceLocator.getInstance().setProperties(appSettings); // merges application-settings and application properties
		} catch (IOException exception) {
			logger.error(String.format("IOException Occured while setting up the Application properties, Exceprion:&s",exception.getMessage()));
			System.exit(-1); // System exit due to Settings failure
		}
	}
}
