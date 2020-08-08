package week.two.examples;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class UpdateTest {
    public static DBCollection createCollection() throws UnknownHostException {
        MongoClient client = new MongoClient();
        DBCollection collection =  client.getDB("course").getCollection("updateTest");  
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
    
    public static void main(String[] args) throws UnknownHostException {
        DBCollection collection = createCollection();
        
        //populate
        List<String> names = Arrays.asList("alice", "bobby", "cathy", "david", "ethan");
        for (String name : names) {
            collection.insert(new BasicDBObject("_id", name));
        }
        
        printCollection(collection);
        
        //add a field called age to alice
        DBObject query = new BasicDBObject("_id", "alice");
        collection.update(query, new BasicDBObject("age", 24)); //replaces entire document including ID
        printCollection(collection);
        
        //add a field called gender to alice
        collection.update(query, new BasicDBObject("gender", "F")); //age disappears, because document is replaced
        printCollection(collection);
        
        //keep age by adding gender using $set
        collection.update(query, new BasicDBObject("age", 24));
        collection.update(query, new BasicDBObject("$set", new BasicDBObject("gender", "F")));
        printCollection(collection);
        
        //try to update where id doesn't exist
        query = new BasicDBObject("_id", "frank");
        collection.update(query, new BasicDBObject("$set", new BasicDBObject("gender", "M")));
        printCollection(collection);
        
        //now try with upsert (inserts it if not present) //1: query, 2: update doc, 3: upsert, 4: multi
        collection.update(query, new BasicDBObject("$set", new BasicDBObject("gender", "M")), true, false); 
        printCollection(collection);
        
        //multi insert
        query = new BasicDBObject(); //all documents
        collection.update(query, new BasicDBObject("$set", new BasicDBObject("title", "Dr.")), false, true); 
        printCollection(collection);
        
        //remove - takes a doc that represents a query (remove alice)
        query = new BasicDBObject("_id", "alice");
        collection.remove(query);
        printCollection(collection);
        
        //remove all
        collection.remove(new BasicDBObject());
        printCollection(collection);
    }
}