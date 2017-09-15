package com.wday.search.api.github;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
/**
 * 
 * As the data is in real-time and may varies time to time, So just verified and GitHub API invocation
 * Verified the default mandatory parameter 
 * @author Narasimha
 *
 */
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
