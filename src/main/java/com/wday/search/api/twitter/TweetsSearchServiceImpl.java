package com.wday.search.api.twitter;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wday.search.api.exception.SearchAPICustomException;
import com.wday.search.api.util.ApiType;
import com.wday.search.api.util.HttpsURLConnectionBuilder;
import com.wday.search.api.util.PropertyServiceLocator;
/**
 * Invokes Twitter API to retrieve the parameters
 * 
 * @author Narasimha
 *
 */
public class TweetsSearchServiceImpl implements TweetsSearchService {
	
	private static Log logger = LogFactory.getLog(TweetsSearchServiceImpl.class);

	/**
	 * Searches the recent Tweet Texts by GitHubProjectName
	 * @return JsonObject Twitter API Response
	 * 
	 */
	public JSONObject searchRecentTweetsByGitHubProject(
			@NotNull(message = "Repository Names should not be NULL") final String gitHubProjectName) { 
		HttpsURLConnection httpsURLConection = null;
		JSONObject json = null;
		JSONObject tweetMessages = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			httpsURLConection = HttpsURLConnectionBuilder.buildConnection(ApiType.Twitter, gitHubProjectName);
			
			String validatedMessage = HttpsURLConnectionBuilder.validateStatuCode(httpsURLConection.getResponseCode());
			if(validatedMessage!=null && validatedMessage.length()>0) {
				throw new SearchAPICustomException("107", String.format("Exception Occured at Twitter API Service : %s",validatedMessage));
			}
			
			StringBuilder response = new StringBuilder(IOUtils.toString(httpsURLConection.getInputStream(), "UTF-8"));
			if (response != null) {
				json = (JSONObject) new JSONObject(response.toString());
				jsonArray = json.getJSONArray("statuses");
				for (int index = 0; index < jsonArray.length(); index++) {
					tweetMessages.put("text", ((JSONObject) jsonArray.get(index)).get("text"));
					tweetMessages.put("screen_name",
							((JSONObject) ((JSONObject) jsonArray.get(index)).get("user")).get("screen_name"));
					String tweetLimit = PropertyServiceLocator.getInstance().getProperty("tweets.limit");
					
					if (index < validateNoTweets(tweetLimit) )
						break;
				}
			}
			} catch (IOException | JSONException twitterServiceException) {
			logger.error(String.format("Failed to search tweets:Exception Occured -> searchRecentTweetsByKeywords:"
					+ twitterServiceException.getMessage()));
			twitterServiceException.printStackTrace();
			throw new SearchAPICustomException("102", String.format("Exception Occured at Twitter API Service : %s",twitterServiceException.getMessage()));
		} finally {
			if (httpsURLConection != null)
				httpsURLConection.disconnect();
		}
		return tweetMessages;
	}
	
	private int validateNoTweets(String tweetLimit ) {
		try {
			if(tweetLimit!=null && tweetLimit.length() > 0)
				 return  Integer.parseInt(tweetLimit);
			} catch (NumberFormatException exception) {
				logger.error("Validation failed at No of Tweets, setting default value");
			}
		return 10;
	}
}
