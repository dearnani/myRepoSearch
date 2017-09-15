package com.wday.search.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppSettingUpdateUtil {

	private static Log logger = LogFactory.getLog(AppSettingUpdateUtil.class);
	private static Properties appSettings = new Properties();
	
	public static void applyApplicationSettings()
	{
		
		appSettings = System.getProperties();
		String fileName = System.getProperty("settingsFile");
		
		if(fileName == null)
			fileName="app-settings.properties";
		System.out.println(String.format("FileName:%s", fileName));
		
		try (InputStream settingFileStream = AppSettingUpdateUtil.class.getClassLoader().getResourceAsStream(fileName)) {
			appSettings.load(settingFileStream);
			PropertyServiceLocator.getInstance().setProperties(appSettings);
		} catch (IOException exception) {
			logger.error("IOException Occured while setting up the Application properties");
			System.exit(-1);
		}
	}
}
