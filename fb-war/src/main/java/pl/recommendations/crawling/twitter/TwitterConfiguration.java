package pl.recommendations.crawling.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConfiguration {
    private static TwitterFactory tf;

    public static Twitter getTwitterInstance() {
        if (tf == null)
            initTwitterFactpry();

        return tf.getInstance();
    }

    private static void initTwitterFactpry() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("G49QFGyYnqHVWWJkeLTrhcso4")
                .setOAuthConsumerSecret("id5ZK8qLTNGuZeQVj4Y2FtwdSK3MCoGLjOGm073il7CY52QnHb")
                .setOAuthAccessToken("2638728805-lROVdLyesU2HP70AdoWnJJGZhMqB69CuM5iCzEg")
                .setOAuthAccessTokenSecret("YrBBEyE8v7aFfXDfoJavkGKUjcdfogvVgdKMsDZaCTMy0");
        tf = new TwitterFactory(cb.build());
    }

    private TwitterConfiguration() {
    }
}
