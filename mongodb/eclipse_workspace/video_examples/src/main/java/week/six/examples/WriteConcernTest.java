package week.six.examples;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException.DuplicateKey;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class WriteConcernTest {
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
		
		//option 1:  if want all writes to default to journal, can set it at mongo client level 
		//w=1, wtimout=0, fsync=false, j=true
		client.setWriteConcern(WriteConcern.JOURNALED);
		
		DB db = client.getDB("course");
		
		//option 2:  if want all writes to default to journal, can set it at db level
		db.setWriteConcern(WriteConcern.JOURNALED);
		
		DBCollection collection = db.getCollection("write.test");
		
		//option 3:  if want all writes to default to journal, can set it at collection level
		collection.setWriteConcern(WriteConcern.JOURNALED);
		
		collection.drop();
		
		DBObject document = new BasicDBObject("_id", 1);
		collection.insert(document);
		
		//insert another (with a duplicate key)
		try {
			//option 4:  if want all writes to default to journal, can do it for an individual insert
			collection.insert(document, WriteConcern.JOURNALED);
		} catch (DuplicateKey e) {
			System.out.println(e.getMessage());
		} 

		System.out.println("Ending...");
	}
}