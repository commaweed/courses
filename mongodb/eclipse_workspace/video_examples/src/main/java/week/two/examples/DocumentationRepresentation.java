package week.two.examples;

import java.util.Arrays;
import java.util.Date;

import com.mongodb.BasicDBObject;

public class DocumentationRepresentation {
    public static void main(String[] args) {
        BasicDBObject doc = new BasicDBObject();
        doc.put("username", "yourname");
        doc.put("birthdate", new Date(39282345));
        doc.put("programmer", true);
        doc.put("age", 8);
        doc.put("languages", Arrays.asList("Java", "C++"));
        
        //add sub-document
        doc.put("address", new BasicDBObject("street", "20 Main")
            .append("zip", "90210"));
        
    }
}