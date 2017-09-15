package com.wday.search.api.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * This is main class which handles the GiHub and Twitter Services to access their respective APIs
 * 
 * @author Narasimha
 * @version 1.0
 *
 */
public class SearchAPIManager {

	private static Log logger = LogFactory.getLog(SearchAPIManager.class);
	
	//private static Log logger = LogFactory.getLog(Main.class);
	
	/**
	 * Manage the results of Twitter and GitHub API services, 
	 * Prepares JSON file as end result. 
	 * 
	 */
	public static void manageAPI() {
		try {
			// The key word to search the gitHub Repository
			String apiConfigParam = PropertyServiceLocator.getInstance().getProperty("github.api.search.keyword");
			// Invoke the GitHub API Service
			List<String> gitHubProjectDetails = fetchGitHubRepositoryNames(apiConfigParam);
			// Construct the result JSON
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
					// Get the Twitter API response data 
					tweetMessages = fetchTweetsForRepoNames(actulaGitHubDetails[0].split("/")[0]);
					gitHubJSON.put("TwitterAPIattribs", tweetMessages);
					resultJSON.append("GitHubProjects", gitHubJSON);
				}
			}
			// Save to JsonFile
			printJsonToFile(resultJSON);
			
		} catch( SearchAPICustomException exception) {
			
			logger.error(String.format("Exception Occureed, Exiting the Application ErrorCode:%s Error Meesage %s",exception.getErrCode(),exception.getErrMsg()));
			System.exit(-1);
		}
		
	}
	/**
	 * Saves the JsonObject data into User's Home Directory
	 * 
	 * @param jsonObject to be printed JSONOBject 
	 * 
	 */
	private static void printJsonToFile(JSONObject jsonObject) {
		// Generic Folder to save in to local disk location - User Home Directory
		Path path = Paths.get(String.format("%s\\%s", System.getProperty("user.home"),
				PropertyServiceLocator.getInstance().getProperty("json.fileName")));
		try (BufferedWriter jsonFileWriter = Files.newBufferedWriter(path)) {
			jsonObject.write(jsonFileWriter);
		} catch (IOException | JSONException exception) {
			logger.error(String.format("Exception Occured while saving the resulted Json File: %s", exception.getMessage()));
			throw new SearchAPICustomException("105",String.format("Exception Occured while saving the response to JSON File:%s",exception.getMessage()));
		}
	}
	/**
	 * Invokes Twitter API to access twitter Messages.
	 * 
	 * @param gitHubProjects GitHub Project Name - keyword to the API which sets to q parameter
	 * @return JSONObject with all the twitter messages
	 * 
	 */
	private static JSONObject fetchTweetsForRepoNames(String gitHubProjName)  {
		TweetsSearchService tweetsSearchService = new TweetsSearchServiceImpl();
		JSONObject tweetMessages = tweetsSearchService.searchRecentTweetsForKeyword(gitHubProjName);
		return tweetMessages;
	}

	/**
	 * Invokes GitHub API to get the list of GitHub projects
	 * 
	 * @param apiConfigParam GitHubProjectName value assigned to q for Twitter API
	 * @return GitHub Repository Name
	 */
	private static List<String> fetchGitHubRepositoryNames(String apiConfigParam)  {
		GitHubRepoSearchService gitHubRepoSearchService = new GitHubRepoSearchServiceImpl();
		// apiConfigParam is GitHubProjectName, we can scale it by having ellipsis to send more requests parameters
		List<String> repoNames = gitHubRepoSearchService.searchGiHubRepoByKeyword(apiConfigParam);
		return repoNames;
	}
}