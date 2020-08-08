package week.six.examples;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoException.DuplicateKey;
import com.mongodb.ServerAddress;

public class ReplicaSetFailOverTest {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        System.out.println("Starting...");
        
        /*  when you use a seedlist, the MongoClient starts a background thread that pings all
            of the nodes in the seedlist and any others that it discovers.
         */
        MongoClient client = new MongoClient(
            Arrays.asList(
                new ServerAddress("localhost", 27017),
                new ServerAddress("localhost", 27018),
                new ServerAddress("localhost", 27019)
            )
        );
        
        DBCollection test = client.getDB("course").getCollection("replica.test");
        test.drop();
        
        for (int i = 0; i < 10; i++) {
            for (int retries = 0; retries <= 2; retries++) {
                try {
                    test.insert(new BasicDBObject("_id", i));
                    System.out.println("Inserted document: " + i);
                    break;
                } catch (DuplicateKey e) {
                    //the document may have already inserted
                    System.out.println("Document already inserted: " + i);
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Retrying");
                    Thread.sleep(5000);
                }
            }
            Thread.sleep(500);
        }
        
        System.out.println("Ending...");
    }
}