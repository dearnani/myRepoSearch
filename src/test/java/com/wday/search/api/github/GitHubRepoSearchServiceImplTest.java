package com.wday.search.api.github;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class GitHubRepoSearchServiceImplTest {

	@Test
	public void testSearchGiHubRepoByKeyword() {

		GitHubRepoSearchService gitHubRepoSearchService = new GitHubRepoSearchServiceImpl();
		List<String> repoNames = gitHubRepoSearchService.searchGiHubRepoByKeyword("Reactive");
		
		if(repoNames != null && !repoNames.isEmpty()) {
			
			assertNotNull(repoNames.toString());
		}
	}

}
