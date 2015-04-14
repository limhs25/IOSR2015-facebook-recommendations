package pl.recommendations.crawling.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfiguration {

	private static TwitterFactory tf;

	public static Twitter getTwitterInstance() {
		if (tf == null)
			initTwitterFactory();

		return tf.getInstance();
	}

	private static void initTwitterFactory() {
		ConfigurationBuilder cb = getConfigurationBuilder();
		cb.setOAuthAccessToken("2638728805-lROVdLyesU2HP70AdoWnJJGZhMqB69CuM5iCzEg")
				.setOAuthAccessTokenSecret("YrBBEyE8v7aFfXDfoJavkGKUjcdfogvVgdKMsDZaCTMy0");
		tf = new TwitterFactory(cb.build());
	}

	public static Configuration getAppConfigurationForUsers() {
		return getConfigurationBuilder().build();
	}

	private static ConfigurationBuilder getConfigurationBuilder() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("G49QFGyYnqHVWWJkeLTrhcso4")
				.setOAuthConsumerSecret("id5ZK8qLTNGuZeQVj4Y2FtwdSK3MCoGLjOGm073il7CY52QnHb");
		return cb;
	}

	private TwitterConfiguration() {
	}
}
