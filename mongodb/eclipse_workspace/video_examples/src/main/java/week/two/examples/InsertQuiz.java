package week.two.examples;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class InsertQuiz {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        DB db = client.getDB("school");
        DBCollection people = db.getCollection("people");
        
        people.drop();

        DBObject doc = new BasicDBObject("name", "Andrew Erlichson")
                .append("company", "10gen");

        try {
            people.insert(doc);      // first insert
            doc.removeField("_id");  // remove the "_id" field
            people.insert(doc);      // second insert
        } catch (Exception e) {
            e.printStackTrace();
        }
//        
//        { "_id" : ObjectId("52dcda18e4b02592780364b2"), "name" : "Andrew Erlichson", "company" : "10gen" }
//        { "_id" : ObjectId("52dcda18e4b02592780364b3"), "name" : "Andrew Erlichson", "company" : "10gen" }

    }
}