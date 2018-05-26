package week.two.examples;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

public class FindCriteriaTest {
	
	public static void displayCount(DBCollection collection, DBObject criteria) {
		System.out.println("\nCount:");
		System.out.println(collection.count(criteria));
	}
	
	public static void displayDocuments(DBCollection collection, DBObject criteria) {
		System.out.println("\nDocuments: ");
		DBCursor cursor = collection.find(criteria);
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient();
		DB db = client.getDB("course");
		DBCollection collection =  db.getCollection("findCriteriaTest");
		
		//ensure it is empty
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			collection.insert(
				new BasicDBObject("x", new Random().nextInt(2))
					.append("y", new Random().nextInt(100))
			);
		}
		
		//all documents
		DBObject query = null; 
		displayCount(collection, query);
		displayDocuments(collection, query);
		
		//limit the criteria to x = 0
		query = new BasicDBObject("x", 0); 
		displayCount(collection, query);
		displayDocuments(collection, query);
		
		//limit the criteria to x = 0 and y > 10 and y < 90
		query = new BasicDBObject("x", 0).append("y", new BasicDBObject("$gt", 10).append("$lt", 90)); 
		displayCount(collection, query);
		displayDocuments(collection, query);
		
		//using QueryBuilder
		QueryBuilder queryBuilder = QueryBuilder.start("x").is(0).and("y").greaterThan(70);
		displayCount(collection, queryBuilder.get());
		displayDocuments(collection, queryBuilder.get());		
	}
}