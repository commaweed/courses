package week.two.examples;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class InsertTest3 {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
        DB courseDb = client.getDB("course");
        DBCollection collection =  courseDb.getCollection("insertTest");
        
        //ensure it is empty
        collection.drop();
        
        DBObject document = new BasicDBObject().append("x", 1);
        
        collection.insert(document);
        collection.insert(document);
        
        //Exception in thread "main" com.mongodb.MongoException$DuplicateKey: 
        //E11000 duplicate key error index: course.insertTest.$_id_  dup 
        //key: { : ObjectId('52dcd8d5e4b09cf486dca2fb') 
    }
}