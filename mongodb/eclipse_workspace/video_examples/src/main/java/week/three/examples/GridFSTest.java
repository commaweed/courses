package week.three.examples;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFSTest {
    
    private static final String FILE_NAME = "abbeysong.mpg";
    private static final String FILE_PATH = "/my_large_files/" + FILE_NAME;
    
    public static void main(String[] args) throws UnknownHostException {
        MongoClient client = new MongoClient();
        DB db = client.getDB("course");
        
        InputStream inputStream = GridFSTest.class.getResourceAsStream(FILE_PATH);
        
        GridFS videos = new GridFS(db, "videos");
        GridFSInputFile video = videos.createFile(inputStream, FILE_NAME);
        
        //other metadata
        BasicDBObject metadata = new BasicDBObject("description", "Abbey Singing");
        metadata.append("tags", Arrays.asList("Singing", "home"));
    
        video.setMetaData(metadata);
        video.save();
        
        System.out.println("Object ID in Files Collection [" + video.get("_id") + "]");
        //result: Object ID in Files Collection [52dfb516e4b0b072e04a02e0]
    }
}