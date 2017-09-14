package com.wday.search.api.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.JSONException;

/**
 * 
 */
public class HttpsURLConnectionBuilder {
	
	final static Logger logger = Logger.getLogger(HttpsURLConnectionBuilder.class);
	static Properties configProps = PropertyServiceLocator.getInstance().getProperties();
	
	/**
	 * @param APiType conveys about which API to invoke
	 * @param gitHubProjName the key word to search Tweets using twitter API
	 * 
	 * Builds the respective API's HttpsURLConnection 
	 * 
	 */
	public static HttpsURLConnection buildConnection (ApiType api, String apiParameter)   {
		HttpsURLConnection httpURLConnection = null;
		switch(api) {
		case GitHub: 
			return getGitHubAPIConnection(apiParameter);
		case Twitter:
			return getTwitterAPIConnection(apiParameter);
		}
		return httpURLConnection;
	}
	
	/**
	 * Builds the GitHub API HttpsURLConnection by using configuration parameters
	 * 
	 */
	private static HttpsURLConnection getGitHubAPIConnection(String apiParameter) {
		HttpsURLConnection httpURLConnection = null;
		try {
			URL url = new URL(gitHubURLBuilder(apiParameter));
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
	
	/**
	 * 
	 * Builds the Twitter API HttpsURLConnection by using configuration parameters,
	 * Authenticates using Basic Authentication, Authorizes with Bearer Token.
	 * @param gitHubProjName
	 * @return HttpsURLConnections
	 * 
	 * 
	 * 
	 */
	private static HttpsURLConnection getTwitterAPIConnection(String gitHubProjName) {
		HttpsURLConnection httpsURLConnection = null;
		try {
			String encodedUserCredentials = new String(Base64.encodeBase64(String.format("%s:%s", URLEncoder.encode(configProps.getProperty("oauth.consumerKey").trim(),"UTF-8"),URLEncoder.encode(configProps.getProperty("oauth.consumerSecret"),"UTF-8")).getBytes()));
			URL url = new URL(configProps.getProperty("twitter.api.search.tweets.url")+gitHubProjName);
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setRequestProperty("Host", configProps.getProperty("twitter.api.host.url"));
			httpsURLConnection.setRequestProperty("User-Agent", configProps.getProperty("user.agent"));
			httpsURLConnection.setRequestProperty("Authentication", "Basic " + encodedUserCredentials);
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpsURLConnection.setRequestProperty("Authorization", "Bearer " + "AAAAAAAAAAAAAAAAAAAAAPsG2QAAAAAASpupJSX4kBQHL0c1xxydr2aDCcI%3D06zNsVzup40gNTS1hjtvJ5eUaampVr0jEzAJ5qepj1ShrQ11YA");
			httpsURLConnection.setUseCaches(false);
		} catch ( IOException | JSONException  exception) {
			logger.debug(" Exception Occured while trying to establish HttpsURLConnection for Twitter API Exception:"+exception.getMessage());
		}
		return httpsURLConnection;
	}
	
	/**
	 * Builds the queryString   
	 * @return QueryString to invoke GitHub API
	 */
	private static String gitHubURLBuilder(String apiConfigParam)
	{
		StringBuilder urlBuilder = new StringBuilder(configProps.getProperty("github.api.search.keyword.url"));
		urlBuilder.append(apiConfigParam)
				  .append("&sort=")
				  .append(configProps.getProperty("github.api.search.sort"))
				  .append("&order=")
				  .append(configProps.getProperty("github.api.search.order"));
		return urlBuilder.toString();
	}
}