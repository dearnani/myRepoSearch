package com.wday.search.api.github;

import java.util.List;

import com.wday.search.api.exception.SearchAPICustomException;
/**
 * 
 * @author Narasimha
 *
 */
public interface GitHubRepoSearchService {
	 	 List<String> searchGiHubRepoByKeyword (String apiConfigParam) throws SearchAPICustomException;
}
