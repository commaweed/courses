package week.two.examples;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class FindAndModifyTest {
    public static DBCollection createCollection() throws UnknownHostException {
        MongoClient client = new MongoClient();
        DBCollection collection =  client.getDB("course").getCollection("findAndModifyTest");   
        collection.drop(); //ensure empty
        
        return collection;
    }
    
    public static void printCollection(DBCollection collection) {
        System.out.println("\nDocuments (total docs [" + collection.count() + "]): ");
        DBCursor cursor = collection.find();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }   
    }
    
    public static int getRange(String counterId, int range, DBCollection collection) {
        DBObject document = collection.findAndModify(
            new BasicDBObject("_id", counterId),    //query
            null,                                   //fields
            null,                                   //sort
            false,                                  //remove
            new BasicDBObject("$inc", new BasicDBObject("counter", range)), //update
            true,                                   //return new document
            true                                    //upsert
        );
        
        //document.get("counter") returns value after the range
        
        return (Integer) document.get("counter") - range + 1;
    }
    
    public static void displayRange(int first, int numNeeded) {
        System.out.println("Range: " + first + "-" + (first + numNeeded - 1));
    }
    
    public static void main(String[] args) throws UnknownHostException {
        DBCollection collection = createCollection();
        
        //service example that provides a unique ranges of numbers for a given ID
        
        final String counterId = "abc";
        int first;
        int numNeeded;
        
        numNeeded = 2;
        first = getRange(counterId, numNeeded, collection);
        displayRange(first, numNeeded);
        
        numNeeded = 3;
        first = getRange(counterId, numNeeded, collection);
        displayRange(first, numNeeded);

        numNeeded = 10;
        first = getRange(counterId, numNeeded, collection);
        displayRange(first, numNeeded);
        
        printCollection(collection);
    }
}