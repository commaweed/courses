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

public class FindCriteriaQuiz {
	
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
		DB db = client.getDB("m101");
		DBCollection collection =  db.getCollection("scores");
	
		//shell: db.scores.find({ type: "quiz", score: { $gt:20, $lt:90 } });
		
		//using QueryBuilder
		QueryBuilder queryBuilder = QueryBuilder.start("type").is("quiz").and("score").greaterThan(20).lessThan(90);
		displayCount(collection, queryBuilder.get());
		displayDocuments(collection, queryBuilder.get());		
	}
}