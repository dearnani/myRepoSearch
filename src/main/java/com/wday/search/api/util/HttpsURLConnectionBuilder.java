package com.wday.search.api.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.wday.search.api.exception.SearchAPICustomException;

/**
 * 
 */
public class HttpsURLConnectionBuilder {
	
	private static Log logger = LogFactory.getLog(HttpsURLConnectionBuilder.class);
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
			throw new SearchAPICustomException("103","Exception Occured while estabshling the GitHUB API Service's HttpsURLConnections");
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
			String BeareToken = requestBearerToken(configProps.getProperty("twitter.oauth.api.token"));
			URL url = new URL(String.format("%S%S",configProps.getProperty("twitter.api.search.tweets.url"),gitHubProjName));
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setRequestMethod("GET");
			httpsURLConnection.setRequestProperty("Host", configProps.getProperty("twitter.api.host.url"));
			httpsURLConnection.setRequestProperty("User-Agent", configProps.getProperty("user.agent"));
			httpsURLConnection.setRequestProperty("Authentication", "Basic " + encodedUserCredentials);
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpsURLConnection.setRequestProperty("Authorization", String.format("Bearer %s",  BeareToken)); //+ "AAAAAAAAAAAAAAAAAAAAAPsG2QAAAAAASpupJSX4kBQHL0c1xxydr2aDCcI%3D06zNsVzup40gNTS1hjtvJ5eUaampVr0jEzAJ5qepj1ShrQ11YA");
			httpsURLConnection.setUseCaches(false);
		} catch ( IOException | JSONException  exception) {
			logger.debug(" Exception Occured while trying to establish HttpsURLConnection for Twitter API Exception:"+exception.getMessage());
			throw new SearchAPICustomException("104","Exception Occured while estabshling the Twitter API Service's HttpsURLConnections");
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
	

	/**
	 * Constructs the request for requesting a bearer token and returns that token as a string
	 * 
	 * @param endPointUrl
	 * @return
	 * @throws MalformedURLException
	 * @throws JSONException
	 * @throws IOException
	 * @throws 
	 * 
	 */
	private static String requestBearerToken(String endPointUrl) throws MalformedURLException, JSONException, IOException  {

		HttpsURLConnection httpsURLConnection = null;
		String encodedUserCredentials = new String(Base64.encodeBase64(String.format("%s:%s", URLEncoder.encode(configProps.getProperty("oauth.consumerKey").trim(),"UTF-8"),URLEncoder.encode(configProps.getProperty("oauth.consumerSecret"),"UTF-8")).getBytes()));
		try {
			URL url = new URL(endPointUrl);
			httpsURLConnection = (HttpsURLConnection) url.openConnection();
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setRequestProperty("Host", configProps.getProperty("twitter.api.host.url"));
			httpsURLConnection.setRequestProperty("User-Agent", configProps.getProperty("user.agent"));
			httpsURLConnection.setRequestProperty("Authorization", "Basic " + encodedUserCredentials);
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(encodedUserCredentials.length()));
			httpsURLConnection.setUseCaches(false);

			// write the params to the request
			writeRequest(httpsURLConnection, "grant_type=client_credentials");

			StringBuffer response = new StringBuffer(IOUtils.toString(httpsURLConnection.getInputStream(),"utf-8"));
			JSONObject obj = new JSONObject(response.toString());

			String tokenType = (String) obj.get("token_type");
			String token = (String) obj.get("access_token");

			return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
		} catch (MalformedURLException | JSONException e) {
			logger.error("Exception Occured while retrieving the Bearer Token");
		} finally {
			if (httpsURLConnection != null) {
				httpsURLConnection.disconnect();
			}
		}
		return new String();
	}
	
	/**
	 *  Updates the request
	 * 
	 * @param connection
	 * @param textBody
	 * @return
	 */
	private static boolean writeRequest(HttpsURLConnection connection, String textBody) {
		
		try( BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream())) ) {
			wr.write(textBody);
			wr.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}



}