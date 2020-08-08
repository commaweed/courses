package util;

import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DatabaseUtil {
    
    //prevent instantiation
    private DatabaseUtil() {}
    
    /**
     * Returns a handle for the collection according to the provided criteria.
     * @param dbName The name of the database.
     * @param collectionName The name of the collection.
     * @return DBCollection
     * @throws UnknownHostException
     */
    public static DBCollection getCollection(String dbName, String collectionName) throws UnknownHostException {
        if (StringUtils.trimToNull(dbName) == null) {
            throw new IllegalArgumentException("Invalid dbName; it cannot be null.");
        }
        if (StringUtils.trimToNull(collectionName) == null) {
            throw new IllegalArgumentException("Invalid collectionName; it cannot be null.");
        }       
        return new MongoClient().getDB(dbName).getCollection(collectionName);
    }
}