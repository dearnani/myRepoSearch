package com.wday.search.api.twitter;

import javax.validation.constraints.NotNull;

import org.json.JSONObject;

import com.wday.search.api.exception.SearchAPICustomException;


public interface TweetsSearchService {

	JSONObject searchRecentTweetsByGitHubProject(@NotNull(message = "Repository Names should not be NULL") final String keywords) throws SearchAPICustomException;
	
}
