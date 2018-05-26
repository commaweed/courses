package tjjenk2;

import java.net.UnknownHostException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

public class Homework3_1 {
	
	/**
	 * Get a collection to the school.students
	 * @return DBCollection
	 * @throws UnknownHostException
	 */
	public static DBCollection getCollection() throws UnknownHostException {
		MongoClient client = new MongoClient();
		DBCollection collection =  client.getDB("school").getCollection("students");	
		return collection;
	}
	
	/**
	 * Display the documents according to the provided criteria
	 * @param collection
	 * @param criteria
	 * @param fields
	 * @param sortCriteria
	 * @param skipRows
	 * @param limitRows
	 */
	public static void displayDocuments(
		DBCollection collection,
		DBObject criteria,
		DBObject fields,
		DBObject sortCriteria,
		Integer skipRows,
		Integer limitRows
	) {
		System.out.println("\nDocuments (total docs [" + collection.count() + "]): ");

		DBCursor cursor = collection.find(criteria, fields).sort(sortCriteria);
		
		if (skipRows != null) cursor.skip(skipRows);
		if (limitRows != null) cursor.limit(limitRows);
		
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			
			System.out.println();
		} finally {
			cursor.close();
		}		
	}
	
	/**
	 * Removes the lowest homework score from the given scores list.
	 * @param scores A list of scores.
	 */
	private static void removeLowestScore(BasicDBList scores) {
		BasicDBObject lowestScore = null;
		
		double currentLowest = -1;
		for (Object listItem : scores) {
			BasicDBObject currentScore = (BasicDBObject) listItem;
			
			String type = (String) currentScore.get("type");
			double score = (Double) currentScore.get("score");
			
			if ("homework".equals(type)) {
				if (currentLowest == -1 || score < currentLowest) {
					currentLowest = score;
					lowestScore = currentScore;
				}
			}
		}
		
		if (lowestScore != null) {
			scores.remove(lowestScore);
		}
	}
	
	/**
	 * Main entry.
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = getCollection();
		
		QueryBuilder queryBuilder = QueryBuilder.start("scores.type").is("homework");
		DBObject query = queryBuilder.get();
		DBObject fields = null;
		DBObject sortCriteria = new BasicDBObject("_id", 1);
		Integer skipRows = null;
		Integer limitRows = 20;
		
		//display initial
		displayDocuments(collection, query, fields, sortCriteria, skipRows, limitRows);
		
		//loop and remove lowest homework score
		DBCursor cursor = collection.find(query, fields).sort(sortCriteria);
		try {
			while (cursor.hasNext()) {
				DBObject document = cursor.next();
				removeLowestScore((BasicDBList) document.get("scores"));
				
				//replace the existing document
				collection.update(new BasicDBObject("_id", document.get("_id")), document, false, false); 
			}
		} finally {
			cursor.close();
		}
		
		displayDocuments(collection, query, fields, sortCriteria, skipRows, limitRows);
	}
}