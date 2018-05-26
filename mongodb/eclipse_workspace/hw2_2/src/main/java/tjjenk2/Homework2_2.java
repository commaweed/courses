package tjjenk2;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

public class Homework2_2 {
	/**
	 * Get a collection to the students.grades.
	 * @return
	 * @throws UnknownHostException
	 */
	public static DBCollection getCollection() throws UnknownHostException {
		MongoClient client = new MongoClient();
		DBCollection collection =  client.getDB("students").getCollection("grades");	
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
	 * Main entry.
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		DBCollection collection = getCollection();
		
		QueryBuilder queryBuilder = QueryBuilder.start("type").is("homework");
		DBObject query = queryBuilder.get();
		DBObject fields = null;
		DBObject sortCriteria = new BasicDBObject("student_id", 1).append("score", 1);
		Integer skipRows = null;
		Integer limitRows = 20;
		
		//display initial
		displayDocuments(collection, query, fields, sortCriteria, skipRows, limitRows);
		
		//loop and remove lowest
		int lastStudentId = 0, currentStudentId = 0, loopCounter = 0;
		DBCursor cursor = collection.find(query, fields).sort(sortCriteria);
		try {
			while (cursor.hasNext()) {
				DBObject document = cursor.next();
				currentStudentId = (Integer) document.get("student_id");
				if (loopCounter++ == 0 || currentStudentId != lastStudentId) {
					lastStudentId = currentStudentId;	
					System.out.println("Removing: " + document);
					collection.remove(document);
				}
			}
		} finally {
			cursor.close();
		}
				
		//test: 101st
		/*
		> db.grades.find().sort({'score':-1}).skip(100).limit(1)
		{ "_id" : ObjectId("50906d7fa3c412bb040eb709"), "student_id" : 100, "type" : "homework", "score" : 88.50425479139126 }		 
		 */
		query = new BasicDBObject();
		sortCriteria = new BasicDBObject("score", -1);
		skipRows = 100;
		limitRows = 1;
		displayDocuments(collection, query, fields, sortCriteria, skipRows, limitRows);
		
		//test sort by student_id, score and see top five
		/*
		> db.grades.find({},{'student_id':1, 'type':1, 'score':1, '_id':0}).sort({'student_id':1, 'score':1, }).limit(5)
		{ "student_id" : 0, "type" : "quiz", "score" : 31.95004496742112 }
		{ "student_id" : 0, "type" : "exam", "score" : 54.6535436362647 }
		{ "student_id" : 0, "type" : "homework", "score" : 63.98402553675503 }
		{ "student_id" : 1, "type" : "homework", "score" : 44.31667452616328 }
		{ "student_id" : 1, "type" : "exam", "score" : 74.20010837299897 }
		 */
		fields = new BasicDBObject("student_id", 1).append("type", 1).append("score", 1).append("_id", 0);
		sortCriteria = new BasicDBObject("student_id", 1).append("score", 1);
		skipRows = null;
		limitRows = 5;
		displayDocuments(collection, query, fields, sortCriteria, skipRows, limitRows);		
	}
}