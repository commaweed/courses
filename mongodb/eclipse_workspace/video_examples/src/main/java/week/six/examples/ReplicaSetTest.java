package week.six.examples;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class ReplicaSetTest {
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        System.out.println("Starting...");
        
        //the specified port is for the primary port and if you rs.stepDown(), you will get error 
        //because the primary is no longer the primary.  With replica sets, better to use a seed
        //list.
        //MongoClient will use the seed list to find all the servers in the replicaset and will
        //find the primary even if it is not in the seed list
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
            test.insert(new BasicDBObject("_id", i));
            System.out.println("Inserted document: " + i);
            Thread.sleep(500);
        }
        System.out.println("Ending...");
    }
}