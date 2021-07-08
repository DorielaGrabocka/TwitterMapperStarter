package twitter;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class LiveTwitterSourceConfigurationProvider {
    private static final String API_KEY = "9owga32n2f1trAcNU0Z7f7QKq";
    private static final String API_SECRET_KEY = "x31IcgRLKGUKYbG0oXvv7kbXCKZLLGIP40zjZVv7aEh81qY3He";
    private static final String ACCESS_TOKEN = "1412754939022135298-g8PEc77kmHRaeyctVACuY6HNRyi4Gi";
    private static final String ACCESS_TOKEN_SECRET = "phxyMb52rjWabjvSFlOIFG2TNZE1AyEj8T5DEdqy1O4wQ";


    public static Configuration getLiveTwitterSourceConfiguration(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        return cb.setOAuthConsumerKey(API_KEY)
                .setOAuthConsumerSecret(API_SECRET_KEY)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .build();
    }


}
