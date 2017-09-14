# myRepoSearch



Application Summary:

The application 
	
	Consumes the GitHub API public repository without the credentials.
		GitHubRepoSearchService retrieves the repositoryNames by keyword, attributes like sort and order
	Access the Twitter API by with credentials. The following are the keys to access the Twitter API.
	Bearer key will be generated using the credentials to get the access for the Tweets search by GitHub Project 
		oauth.consumerKey
		oauth.consumerSecret
		
		By creating an application at https://dev.twitter.com	we can get all the above values.
	
	TweetsSearchService fetches the recent tweets by repositoryName.
	Currently to achieve the end to end connectivity the parameters are limited. 
 		
	The created json file will be saved at user home directory to avoid permission issues.
	File Name is configurable.
	
	Used the right User-Agent to confiigure user.agent : http://www.whoishostingthis.com/tools/user-agent/
	
Configuration:
The following are the application specific settings needs to be configured and this file should be in class path which bundles in jar
The given consumerKey and consumerSecret are just show the sample datay, may not work.

	app-settings.properties
		
	github.api.search.keyword=reactive
	github.api.search.sort=stars

	#default is descending order only
	github.api.search.order=desc
	
	oauth.consumerKey=wGxaoagLZdM4tYtTEwesse8GGgqC
	oauth.consumerSecret=uviCwqj2UXuJP3HgJsdfs0GjykPQ3SYY0Lb4evT5vr72NsabSmjdYT
	user.agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36
	
	tweets.limit=10
	
	# will be stored at User Home directory
	json.fileName=Result.json

Save the configuration file under /src/main/resources 

HOW To Run:
	
	To compile the program use the following maven command:
	mvn clain install package -Pprod

	The program compiles into one single jar file that contains the following runnable class:
	com.wday.search.api.core.MainApp

	The program can be run from the command line or with the following Maven command:
	mvn exec:exec

	You can also run the following way to set up the application mandatory configuration properties
	
	mvn exec:exec -DsettingsFile="my-app-settings.properties"
	
	Note: You should keep the my-app-settings.properties under the packaged .jar as a classpath resource.
	
