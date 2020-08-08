package week.two.examples;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class InsertTest2 {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
        DB courseDb = client.getDB("course");
        DBCollection collection =  courseDb.getCollection("insertTest");
        
        //ensure it is empty
        collection.drop();
        
        DBObject document = new BasicDBObject().append("x", 1);
        DBObject document2 = new BasicDBObject("_id", new ObjectId()).append("x", 2);
        
        collection.insert(Arrays.asList(document, document2));
    }
}