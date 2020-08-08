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

public class DotNotationTest {
    
    public static void displayCount(DBCollection collection, DBObject criteria) {
        System.out.println("\nCount:");
        System.out.println(collection.count(criteria));
    }
    
    public static void displayDocuments(DBCollection collection, DBObject criteria, DBObject fields) {
        displayCount(collection, criteria);
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
        DBCollection lines =  db.getCollection("dotNotationTest");
        
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
        displayDocuments(lines, queryBuilder.get(), fields);    
        
        //x > 50 (using dot notation
        queryBuilder = QueryBuilder.start("start.x").greaterThan(50);
        displayDocuments(lines, queryBuilder.get(), fields);
        
        //using with fields too
        fields = new BasicDBObject("start.y", true).append("_id", 0);
        displayDocuments(lines, queryBuilder.get(), fields);
    }
}