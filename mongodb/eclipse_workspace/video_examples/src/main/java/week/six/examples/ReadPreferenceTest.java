package week.six.examples;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class ReadPreferenceTest {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		System.out.println("Starting...");
		
		MongoClient client = new MongoClient(
			Arrays.asList(
				new ServerAddress("localhost", 27017),
				new ServerAddress("localhost", 27018),
				new ServerAddress("localhost", 27019)
			)
		);
		
		//option 1: could set read preference on client
		client.setReadPreference(ReadPreference.nearest());
		
		DB db = client.getDB("course");
		
		//option 2: could set read preference on db
		db.setReadPreference(ReadPreference.nearest());
		
		DBCollection collection = db.getCollection("write.test");
		
		//option 3: could set read preference on collection
		collection.setReadPreference(ReadPreference.nearest());
		
		//option 4: could set read preference on the query
		DBCursor cursor = collection.find().setReadPreference(ReadPreference.nearest());
		try {
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}
		

		System.out.println("Ending...");
	}
}