package com.wday.search.api.twitter;

import javax.validation.constraints.NotNull;

import org.json.JSONObject;


public interface TweetsSearchService {

	JSONObject searchRecentTweetsByGitHubProject(@NotNull(message = "Repository Names should not be NULL") final String keywords);
	
}
