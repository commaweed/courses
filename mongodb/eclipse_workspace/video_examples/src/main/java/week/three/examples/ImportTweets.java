package week.three.examples;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * Note: twitter disabled the rest client or requires authentication
 * {"errors": [{"message": "The Twitter REST API v1 is no longer active. 
 *   Please migrate to API v1.1. https://dev.twitter.com/docs/api/1.1/overview.", "code": 68}]}
 * HTTP status code: 403
 */
public class ImportTweets {
    public static void main(String[] args) throws UnknownHostException, IOException, ParseException {
        final String screenName = args.length > 0 ? args[0] : "MongoDB";
        
        MongoClient client = new MongoClient();
        DBCollection collection = client.getDB("course").getCollection("sidebar");
        
        List<DBObject> tweets = getLatestTweets(screenName);
        if (tweets == null) {
            System.out.println("No Tweets!");
        } else {
            for (DBObject tweet : tweets) {
                massageTweetId(tweet);
                massageTweet(tweet);
                collection.update(new BasicDBObject("_id", tweet.get("_id")), tweet, true, false);
            }   
            
            System.out.println("Tweet count: " + collection.count());
        }
    }
    
    public static void massageTweet(DBObject tweet) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d H:m:s Z y");
        tweet.put("created_at", format.parse((String) tweet.get("created_at")));
        
        DBObject userDocument = (DBObject) tweet.get("user");
        Iterator<String> keyIterator = userDocument.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            if (!("id".equals(key) || "name".equals(key) || "screen_name".equals(key))) {
                keyIterator.remove();
            }
        }
    }
    
    public static void massageTweetId(DBObject tweet) {
        Object id = tweet.get("id");
        tweet.removeField("id");
        tweet.put("_id", id);
    }
     
    public static List<DBObject> getLatestTweets(String screenName) throws IOException {
        final String url = "http://api.twitter.com/1/statuses/user_timeline.json?screen_name=" + 
            screenName + "&include_rts=1";
        System.out.println("Connecting to [" +url + "]");
        
        HttpMethod method = new GetMethod(url);
        String responseBody = null;
        try {
            //display status code
            int statusCode = new HttpClient().executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("\nBad status code: " + method.getStatusLine());
            }

            //display response headers
            System.out.println("\nResponse Headers: ");
            Header[] responseHeaders = method.getResponseHeaders();
            for (Header header : responseHeaders) {
                System.out.print(header);
            }
            
            //display response body
            responseBody = method.getResponseBodyAsString();
            System.out.println("\nResponse Body: [" + responseBody + "]");
        } finally {
            method.releaseConnection();
        }   
        
        List<DBObject> results = null;
        if (responseBody != null) {
            //JSON takes a json string, which can be a single document or a list
            results = (List<DBObject>) JSON.parse(method.getResponseBodyAsString());
        }
        
        return results;
    }
}