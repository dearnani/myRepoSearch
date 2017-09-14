package com.wday.search.api.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class HttpsURLConnectionBuilder {
	
	final static Logger logger = Logger.getLogger(HttpsURLConnectionBuilder.class);
	Properties configProps = PropertyServiceLocator.getInstance().populateAllproperties();
	
	public HttpsURLConnection buildConnection (ApiType api,String... gitHubProjName)   {
		HttpsURLConnection httpURLConnection = null;
		switch(api) {
		case GitHub: 
			return getGitHubAPIConnection();
		case Twitter:
			return getTwitterAPIConnection(gitHubProjName);
		}
		return httpURLConnection;
	}
	
	private HttpsURLConnection getGitHubAPIConnection() {
		HttpsURLConnection httpURLConnection = null;
		try {
			URL url = new URL(girHubURLBuilder());
			httpURLConnection = (HttpsURLConnection) url.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setRequestProperty("Host",configProps.getProperty("github.api.host.url"));
			httpURLConnection.setRequestProperty("User-Agent", configProps.getProperty("user.agent"));
			httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		} catch ( IOException | JSONException  exception) {
			logger.debug(" Exception Occured while trying to establish for GitHub API HttpsURLConnection Exception:"+exception.getMessage());
		}
		return httpURLConnection;

	}
	
	private HttpsURLConnection getTwitterAPIConnection(String[] gitHubProjName) {
		
		HttpsURLConnection httpsURLConnection = null;
		
		try {
			
			String encodedUserCredentials = new String(Base64.encodeBase64(String.format("%s:%s", URLEncoder.encode(configProps.getProperty("oauth.consumerKey").trim(),"UTF-8"),URLEncoder.encode(configProps.getProperty("oauth.consumerSecret"),"UTF-8")).getBytes()));
			URL url = new URL(configProps.getProperty("twitter.api.search.tweets.url")+gitHubProjName[0]);
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setRequestProperty("Host", configProps.getProperty("twitter.api.host.url"));
			httpsURLConnection.setRequestProperty("User-Agent", configProps.getProperty("user.agent"));
			httpsURLConnection.setRequestProperty("Authentication", "Basic " + encodedUserCredentials);
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			httpsURLConnection.setRequestProperty("Authorization", "Bearer " + "AAAAAAAAAAAAAAAAAAAAAPsG2QAAAAAASpupJSX4kBQHL0c1xxydr2aDCcI%3D06zNsVzup40gNTS1hjtvJ5eUaampVr0jEzAJ5qepj1ShrQ11YA");
			//httpsURLConnection.setRequestProperty()
			httpsURLConnection.setUseCaches(false);
			
		} catch ( IOException | JSONException  exception) {
			exception.printStackTrace();
			logger.debug(" Exception Occured while trying to establish HttpsURLConnection for Twitter API Exception:"+exception.getMessage());
		}
		return httpsURLConnection;
	}
	
	private String girHubURLBuilder()
	{
		StringBuilder urlBuilder = new StringBuilder(configProps.getProperty("github.api.search.keyword.url"));
		urlBuilder.append(configProps.getProperty("github.api.search.keyword"))
				  .append("&sort=")
				  .append(configProps.getProperty("github.api.search.sort"))
				  .append("&order=")
				  .append(configProps.getProperty("github.api.search.order"));
		return urlBuilder.toString();
	}
}