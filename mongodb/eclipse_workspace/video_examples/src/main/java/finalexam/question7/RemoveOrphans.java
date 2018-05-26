package finalexam.question7;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import util.DatabaseUtil;
import util.QueryCriteria;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class RemoveOrphans {
	
	/**
	 * The album cache where the key is image_id and the value is a collection of 
	 * album_ids that have that image.
	 */
	private static final HashMap<Integer, Set<Integer>> ALBUM_CACHE = new HashMap<>();
	
	private static void populateAlbumCache() throws UnknownHostException {
		System.out.println("Populating ALBUM_CACHE...");
		//query the entire collection
		DBObject query = new BasicDBObject();
		
		QueryCriteria queryCriteria = new QueryCriteria(DatabaseUtil.getCollection("photo", "albums"));
		queryCriteria.setQuery(query);
//		queryCriteria.setLimitRows(10);
		
		//submit the query
		DBCursor cursor = queryCriteria.find();
		try {
			while (cursor.hasNext()) {
				DBObject albumDocument = cursor.next();
				Integer albumId = (Integer) albumDocument.get("_id");
				BasicDBList imageList = (BasicDBList) albumDocument.get("images");
				for (int i = 0; i < imageList.size(); i++) {
					Integer imageId = (Integer) imageList.get(i);
					
					Set<Integer> albumIdSet = ALBUM_CACHE.get(imageId);
					if (albumIdSet == null) {
						albumIdSet = new HashSet<>();
						ALBUM_CACHE.put(imageId, albumIdSet);
					} else {
						//apparently an image can only belong to one album in these collections
						//oh well leaving it as is
						System.out.println("an image belongs to more than one album ["+albumIdSet+"]");
					}
					
					albumIdSet.add(albumId);
				}
			}
		} finally {
			cursor.close();
		}
		
		System.out.println("ALBUM_CACHE populated with size [" + ALBUM_CACHE.size() + "]");
	}
	
	private static void removeOrphans() throws UnknownHostException {
		//query the entire collection
		DBObject query = new BasicDBObject();
		
		DBCollection collection = DatabaseUtil.getCollection("photo", "images");
		
		QueryCriteria queryCriteria = new QueryCriteria(collection);
		queryCriteria.setQuery(query);
//		queryCriteria.setLimitRows(10);
		
		//submit the query
		DBCursor cursor = queryCriteria.find();
		try {
			while (cursor.hasNext()) {
				DBObject imageDocument = cursor.next();
				Integer imageId = (Integer) imageDocument.get("_id");
				
				Set<Integer> set = ALBUM_CACHE.get(imageId);
				if (set == null) {
					//it's an orphan
					System.out.println("Removing orphan, imageId [" + imageId + "]");
					
					//remove from collection
					collection.remove(imageDocument);
				} 
			}
		} finally {
			cursor.close();
		}		
	}
	
	public static void main(String[] args) throws UnknownHostException {
		System.out.println("Starting...");
		populateAlbumCache();
		removeOrphans();
		System.out.println("Finished...");
	}
}