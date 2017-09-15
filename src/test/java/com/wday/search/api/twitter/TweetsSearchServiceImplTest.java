package com.wday.search.api.twitter;

import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Test;

public class TweetsSearchServiceImplTest {

	@Test
	public void testSearchRecentTweetsByGitHubProject() {

		TweetsSearchService tweetsSearchService = new TweetsSearchServiceImpl();
		JSONObject tweetMessages = tweetsSearchService.searchRecentTweetsForKeyword("ReactiveX");
		
		assertNotNull(tweetMessages);
		
		if(tweetMessages!=null && tweetMessages.length()>0)
		{
			for(int index = 0; index < tweetMessages.length(); index++) 
			{
				String tweetMessage = (String)tweetMessages.get("text");
				assertNotNull(tweetMessage);
			}
		}

	}

}
