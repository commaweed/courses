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

public class SortTest {
	
	public static void displayCount(DBCollection collection, DBObject criteria) {
		System.out.println("\nCount:");
		System.out.println(collection.count(criteria));
	}
	
	public static void displayDocuments(
		DBCollection collection,
		DBObject criteria,
		DBObject fields,
		DBObject sortCriteria,
		Integer skipRows,
		Integer limitRows
	) {
		displayCount(collection, criteria);
		System.out.println("\nDocuments: ");
		DBCursor cursor = collection.find(criteria, fields).sort(sortCriteria);
		
		if (skipRows != null) cursor.skip(skipRows);
		if (limitRows != null) cursor.limit(limitRows);
		
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
		DBCollection lines =  db.getCollection("sortTest");
		
		//random seed
		Random rand = new Random();
		
		//ensure it is empty
		lines.drop();
		
		//populate
		for (int i = 0; i < 10; i++) {
			lines.insert(
				new BasicDBObject("_id", i)
					.append("start", 
						new BasicDBObject("x", rand.nextInt(90) + 10).append("y", rand.nextInt(90) + 10)
					)
					.append("end", 
						new BasicDBObject("x", rand.nextInt(90) + 10).append("y", rand.nextInt(90) + 10)
					)
			);
		}
		
		//empty query (all results)
		QueryBuilder queryBuilder = QueryBuilder.start();
		DBObject fields = null;
		DBObject sortCriteria = null;
		Integer numberOfRowsToSkip = null;
		Integer numberOfRowsToLimit = null;
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);	
		
		//x > 50 (using dot notation) (sort ascending)
		queryBuilder = QueryBuilder.start("start.x").greaterThan(50);
		sortCriteria = new BasicDBObject("x", 1);
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);
		
		//using with fields too (sort descending)
		fields = new BasicDBObject("start.y", true).append("_id", 0);
		sortCriteria = new BasicDBObject("y", -1);
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);
		
		//skip first 2 rows
		sortCriteria = new BasicDBObject("y", -1);
		numberOfRowsToSkip = 2;
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);
		
		//limit to first three after skipping 2
		numberOfRowsToLimit = 3;
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);		
		
		//sort on start.y, descending
		sortCriteria = new BasicDBObject("start.y", -1);
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);	
		
		//sort on start.x then start.y
		fields = null;
		sortCriteria = new BasicDBObject("start.x", 1).append("start.y", -1);
		displayDocuments(lines, queryBuilder.get(), fields, sortCriteria, numberOfRowsToSkip, numberOfRowsToLimit);	
	}
}