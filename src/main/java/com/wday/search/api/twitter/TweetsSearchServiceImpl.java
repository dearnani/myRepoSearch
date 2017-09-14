package com.wday.search.api.twitter;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wday.search.api.util.ApiType;
import com.wday.search.api.util.HttpsURLConnectionBuilder;
/**
 * 
 * @author Narasimha
 *
 */
public class TweetsSearchServiceImpl implements TweetsSearchService {
	final static Logger logger = Logger.getLogger(TweetsSearchServiceImpl.class);

	/**
	 * Searches the recent tweets by GitHubProjectName
	 * @return JsonObject
	 */
	public JSONObject searchRecentTweetsByGitHubProject(
			@NotNull(message = "Repository Names should not be NULL") final String gitHubProjectName) { 
		HttpsURLConnection httpsURLConection = null;
		JSONObject json = null;
		JSONObject tweetMessages = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		try {
			httpsURLConection = HttpsURLConnectionBuilder.buildConnection(ApiType.Twitter, gitHubProjectName.split("/")[0]);
			StringBuilder response = new StringBuilder(IOUtils.toString(httpsURLConection.getInputStream(), "UTF-8"));
			if (response != null) {
				json = (JSONObject) new JSONObject(response.toString());
				jsonArray = json.getJSONArray("statuses");
				for (int index = 0; index < jsonArray.length(); index++) {
					tweetMessages.put("text", ((JSONObject) jsonArray.get(index)).get("text"));
					tweetMessages.put("screen_name",
							((JSONObject) ((JSONObject) jsonArray.get(index)).get("user")).get("screen_name"));
					if (index < 10)
						break;
				}
			}
			logger.debug("TweetsSearchServiceImpl->" + tweetMessages.toString());
		} catch (IOException | JSONException twitterServiceException) {
			logger.error(String.format("Failed to search tweets:Exception Occured -> searchRecentTweetsByKeywords:"
					+ twitterServiceException.getMessage()));
		} finally {
			if (httpsURLConection != null)
				httpsURLConection.disconnect();
		}
		return tweetMessages;
	}
}