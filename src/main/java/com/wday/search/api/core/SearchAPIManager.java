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

import com.wday.search.api.github.GitHubRepoSearchService;
import com.wday.search.api.github.GitHubRepoSearchServiceImpl;
import com.wday.search.api.twitter.TweetsSearchService;
import com.wday.search.api.twitter.TweetsSearchServiceImpl;
import com.wday.search.api.util.PropertyServiceLocator;
/**
 * 
 * @author Narasimha
 *
 */
public class SearchAPIManager {
	
	final static Logger logger = Logger.getLogger(SearchAPIManager.class);

	public static void main(String[] args)  {
		
		List<String> gitHubProjectDetails = fetchGitHubRepositoryNames();
		
		JSONObject resultJSON = new JSONObject();
		JSONObject tweetMessages = new JSONObject();
		String[] actulaGitHubDetails = { "","","" };
		
		
		for( String gitHubDetails : gitHubProjectDetails)
		{
			actulaGitHubDetails =  gitHubDetails.split(":");
			
			if(actulaGitHubDetails != null ) {
				JSONObject gitHubJSON = new JSONObject();
				gitHubJSON.put("full_name",actulaGitHubDetails[0]);
				
				if(actulaGitHubDetails[1] != null)
					gitHubJSON.put("language",actulaGitHubDetails[1]);
				else
					gitHubJSON.put("language","");

				tweetMessages = fetchTweetsForRepoNames(actulaGitHubDetails[0].split("/")[0]);
				gitHubJSON.put("TwitterAPIattribs", tweetMessages);
				
				resultJSON.append("GitHubProjects",gitHubJSON);

			}
		}
		
		printJsonToFile(resultJSON);
		
	}

	private static void printJsonToFile(JSONObject jsonObject) {
		//Get the file reference
		Path path = Paths.get( String.format("%s\\%s", System.getProperty("user.home"), PropertyServiceLocator.getInstance().getProperty("json.fileName")));
	
		try (BufferedWriter jsonFileWriter = Files.newBufferedWriter(path)) {
			jsonObject.write(jsonFileWriter);
		} catch (IOException | JSONException exception) {
			logger.error(String.format("Exception Occured while saving the resulted Json File: %s",exception.getMessage()));
		}
	}

	private static JSONObject fetchTweetsForRepoNames(String gitHubProjects) {
		TweetsSearchService tweetsSearchService = new TweetsSearchServiceImpl();
		JSONObject tweetMessages = tweetsSearchService.searchRecentTweetsByKeywords(gitHubProjects);
		//logger.debug(String.format("JSON:: %s", jsonObject));
		return tweetMessages;
	}

	private static List<String> fetchGitHubRepositoryNames() {
		GitHubRepoSearchService gitHubRepoSearchService = new GitHubRepoSearchServiceImpl();
		List<String> repoNames = gitHubRepoSearchService.searchGiHubRepoByKeyword();
		logger.debug(String.format("repoNames::", repoNames.toString()));
		return repoNames;
	}
}