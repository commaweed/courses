package week.two.examples;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class FindTest {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        DB db = client.getDB("course");
        DBCollection collection =  db.getCollection("findTest");
        
        //ensure it is empty
        collection.drop();
        
        //populate
        for (int i = 0; i < 10; i++) {
            collection.insert(new BasicDBObject("x", new Random().nextInt(100)));
        }
        
        
        System.out.println("Find One:");
        DBObject one = collection.findOne();
        System.out.println(one);
        
        //with 10 documents, server cursor will end; but with large collections, definitely close
        System.out.println("\nFind all: ");
        DBCursor cursor = collection.find();
        try {
            while (cursor.hasNext()) {
                DBObject nextDoc = cursor.next();
                System.out.println(nextDoc);
            }
        } finally {
            cursor.close();
        }
        
        
        System.out.println("\nCount:");
        long count = collection.count();
        System.out.println(count);
    }
}

/*
Find One:
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a73e"} , "x" : 46}

Find all: 
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a73e"} , "x" : 46}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a73f"} , "x" : 48}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a740"} , "x" : 33}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a741"} , "x" : 63}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a742"} , "x" : 76}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a743"} , "x" : 44}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a744"} , "x" : 13}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a745"} , "x" : 91}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a746"} , "x" : 73}
{ "_id" : { "$oid" : "52dcdcb4e4b08280f147a747"} , "x" : 96}

Count:
10
*/