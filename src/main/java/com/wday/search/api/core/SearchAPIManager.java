package com.wday.search.api.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.wday.search.api.exception.SearchAPICustomException;
import com.wday.search.api.github.GitHubRepoSearchService;
import com.wday.search.api.github.GitHubRepoSearchServiceImpl;
import com.wday.search.api.twitter.TweetsSearchService;
import com.wday.search.api.twitter.TweetsSearchServiceImpl;
import com.wday.search.api.util.PropertyServiceLocator;

/**
 * 
 * @author Narasimha
 * 
 * This is main class which handles the GiHub and Twitter Services to access their respective APIs
 * 
 *
 */
public class SearchAPIManager {

	final static Logger logger = Logger.getLogger(SearchAPIManager.class);
	
	//private static Log logger = LogFactory.getLog(Main.class);
	
	/**
	 * Manage the result of Twitter and GitHub API services, 
	 * JSON preparation. 
	 * @param args
	 */
	public static void manageAPI() {
		try {
			
			String apiConfigParam = PropertyServiceLocator.getInstance().getProperty("github.api.search.keyword"); 
			List<String> gitHubProjectDetails = fetchGitHubRepositoryNames(apiConfigParam);
			JSONObject resultJSON = new JSONObject();
			JSONObject tweetMessages = new JSONObject();
			String[] actulaGitHubDetails = { "", "", "" };
			for (String gitHubDetails : gitHubProjectDetails) {
				actulaGitHubDetails = gitHubDetails.split(":");
				if (actulaGitHubDetails != null) {
					JSONObject gitHubJSON = new JSONObject();
					gitHubJSON.put("full_name", actulaGitHubDetails[0]);
					if (actulaGitHubDetails[1] != null)
						gitHubJSON.put("language", actulaGitHubDetails[1]);
					else
						gitHubJSON.put("language", "");
					tweetMessages = fetchTweetsForRepoNames(actulaGitHubDetails[0].split("/")[0]);
					gitHubJSON.put("TwitterAPIattribs", tweetMessages);
					resultJSON.append("GitHubProjects", gitHubJSON);
				}
			}
			printJsonToFile(resultJSON);
			
		} catch( SearchAPICustomException exception) {
			
			logger.error(String.format("Exception Occureed ErrorCode:%s Error Meesage",exception.getErrCode(),exception.getErrMsg()));
		}
		
	}
	/**
	 * 
	 * Saves the JsonObject data into User's Home Directory
	 * @param jsonObject 
	 * 
	 */
	private static void printJsonToFile(JSONObject jsonObject) {
		Path path = Paths.get(String.format("%s\\%s", System.getProperty("user.home"),
				PropertyServiceLocator.getInstance().getProperty("json.fileName")));
		try (BufferedWriter jsonFileWriter = Files.newBufferedWriter(path)) {
			jsonObject.write(jsonFileWriter);
		} catch (IOException | JSONException exception) {
			logger.error(String.format("Exception Occured while saving the resulted Json File: %s", exception.getMessage()));
			throw new SearchAPICustomException("105","Exception Occured while saving the response to JSON File");
		}
	}
	/**
	 * 
	 * Invokes Twitter API to access twitter Messages.
	 * @param gitHubProjects GitHub Project Name
	 * @return JSONObject with all the twitter messages
	 * 
	 */
	private static JSONObject fetchTweetsForRepoNames(String gitHubProjects)  {
		TweetsSearchService tweetsSearchService = new TweetsSearchServiceImpl();
		JSONObject tweetMessages = tweetsSearchService.searchRecentTweetsByGitHubProject(gitHubProjects);
		return tweetMessages;
	}

	/**
	 * Invokes GitHub API to get the list of GitHub projects
	 * @return List of GitHub Repository Names
	 */
	private static List<String> fetchGitHubRepositoryNames(String... apiConfigParam)  {
		GitHubRepoSearchService gitHubRepoSearchService = new GitHubRepoSearchServiceImpl();
		List<String> repoNames = gitHubRepoSearchService.searchGiHubRepoByKeyword(apiConfigParam[0]);
		return repoNames;
	}
}