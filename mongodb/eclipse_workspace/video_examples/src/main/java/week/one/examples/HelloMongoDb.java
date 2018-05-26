package week.one.examples;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class HelloMongoDb {
	public static void main(String[] args) throws UnknownHostException {
		System.out.println("Starting...");
//		MongoClient client = new MongoClient("localhost", 27017);
		MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		
		//get a database
		DB database = client.getDB("course");
		
		//get a collection
		DBCollection collection = database.getCollection("hello");
		
		//find one
		DBObject document = collection.findOne();
		System.out.println(document);
		
		System.out.println("finished!");
	}
}