package com.wday.search.api.core;

import com.wday.search.api.util.AppSettingUpdateUtil;
/**
 * 
 * @author Narasimha
 *
 */
public class MainApp {
	/**
	 * Main method to launch the Application
	 * @param args
	 */
	public static void main(String[] args) {
		
		AppSettingUpdateUtil.applyApplicationSettings();		
		SearchAPIManager.manageAPI();
	}
}
