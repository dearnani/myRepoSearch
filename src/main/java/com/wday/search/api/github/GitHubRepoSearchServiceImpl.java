package com.wday.search.api.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
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
	 * Searches the GitHub Repository 
	 */
	public List<String> searchGiHubRepoByKeyword(String apiConfigParam) {
		HttpsURLConnection httpsURLConection = null;
		List<String> repositoryProjects = new ArrayList<String>();
		JSONArray gitHubProjects;
		JSONObject json;
		try {
			httpsURLConection = HttpsURLConnectionBuilder.buildConnection(ApiType.GitHub,apiConfigParam);
			StringBuffer response = new StringBuffer(IOUtils.toString(httpsURLConection.getInputStream(), "UTF-8"));
			if (response != null) {
				gitHubProjects = new JSONObject(response.toString()).getJSONArray("items");
				for (int index = 0; index < gitHubProjects.length(); index++) {
					json = (JSONObject) gitHubProjects.get(index);
					repositoryProjects.add(
							String.format("%s:%s", json.get("full_name").toString(), json.get("language").toString()));
				}
			}
		} catch (IOException ioException) {
			logger.error(
					String.format("GitHubRepoSearchServiceImpl->searchGiHubRepoByKeyword: IOExcepion.getMessage(): %s",
							ioException.getMessage()));
			throw new SearchAPICustomException("101","Exception Occured at the GitHub API Service");
		} finally {
			if (httpsURLConection != null)
				httpsURLConection.disconnect();
		}
		return repositoryProjects;
	}
}