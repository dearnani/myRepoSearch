package com.wday.search.api.core;

import com.wday.search.api.util.AppSettingUpdateUtil;

public class MainApp {
	
	public static void main(String[] args) {
		
		AppSettingUpdateUtil.applyApplicationSettings();		
		SearchAPIManager.manageAPI();
	}
}
