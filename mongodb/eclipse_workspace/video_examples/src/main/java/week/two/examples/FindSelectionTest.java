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

public class FindSelectionTest {
	
	public static void displayCount(DBCollection collection, DBObject criteria) {
		System.out.println("\nCount:");
		System.out.println(collection.count(criteria));
	}
	
	public static void displayDocuments(DBCollection collection, DBObject criteria, DBObject fields) {
		System.out.println("\nDocuments: ");
		DBCursor cursor = collection.find(criteria, fields);
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
		DBCollection collection =  db.getCollection("findSelectionTest");
		
		//random seed
		Random rand = new Random();
		
		//ensure it is empty
		collection.drop();
		
		//populate
		for (int i = 0; i < 10; i++) {
			collection.insert(
				new BasicDBObject("x", rand.nextInt(2))
					.append("y", rand.nextInt(100))
					.append("z", rand.nextInt(1000))
			);
		}
		
		//using QueryBuilder (not showing the x field)
		QueryBuilder queryBuilder = QueryBuilder.start("x").is(0).and("y").greaterThan(10).lessThan(70);
		DBObject fields = new BasicDBObject("x", false);
		displayCount(collection, queryBuilder.get());
		displayDocuments(collection, queryBuilder.get(), fields);	
		
		//using QueryBuilder (only showing the y field)
		fields = new BasicDBObject("y", true).append("_id", false);
		displayCount(collection, queryBuilder.get());
		displayDocuments(collection, queryBuilder.get(), fields);
	}
}