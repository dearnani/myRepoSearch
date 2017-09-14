package com.wday.search.api.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wday.search.api.exception.SearchAPICustomException;
import com.wday.search.api.util.ApiType;
import com.wday.search.api.util.HttpsURLConnectionBuilder;

/**
 * 
 * @author Narasimha
 * 
 *
 */
public class GitHubRepoSearchServiceImpl implements GitHubRepoSearchService {
	private static Log logger = LogFactory.getLog(GitHubRepoSearchServiceImpl.class);
	/**
	 * Searches the GitHub Repository by given keyword as apiConfigParam
	 */
	public List<String> searchGiHubRepoByKeyword(String apiConfigParam) {
		HttpsURLConnection httpsURLConection = null;
		List<String> repositoryProjects = new ArrayList<String>();
		JSONArray gitHubProjects;
		JSONObject json;
		try {
			// prepare and invoke the httpsURLConnection
			httpsURLConection = HttpsURLConnectionBuilder.buildConnection(ApiType.GitHub,apiConfigParam);
			String validatedMessage = HttpsURLConnectionBuilder.validateStatuCode(httpsURLConection.getResponseCode());
			if(validatedMessage!=null && validatedMessage.length()>0) {
				throw new SearchAPICustomException("108", String.format("Exception Occured at Twitter API Service : %s",validatedMessage));
			}
			StringBuffer response = new StringBuffer(IOUtils.toString(httpsURLConection.getInputStream(), "UTF-8"));
			if (response != null) {
				gitHubProjects = new JSONObject(response.toString()).getJSONArray("items");
				// saves the response with individual fields
				for (int index = 0; index < gitHubProjects.length(); index++) {
					json = (JSONObject) gitHubProjects.get(index);
					repositoryProjects.add(
							String.format("%s:%s", json.get("full_name").toString(), json.get("language").toString()));
				}
			}
		} catch (IOException | JSONException exception) {
			logger.error(
					String.format("GitHubRepoSearchServiceImpl->searchGiHubRepoByKeyword: IOExcepion: %s",
							exception.getMessage()));
			throw new SearchAPICustomException("101", String.format("Exception Occured at the GitHub API Service:%s",exception.getMessage()));
		} finally {
			if (httpsURLConection != null)
				httpsURLConection.disconnect();
		}
		return repositoryProjects;
	}
}