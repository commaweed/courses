package week.four.examples;

import java.net.UnknownHostException;

import util.DatabaseUtil;
import util.QueryCriteria;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class Hinting {
    public static void main(String[] args) throws UnknownHostException {    
        QueryBuilder queryBuilder = QueryBuilder.start("a").is(4000).and("b").is(4000).and("c").is(4000);
        DBObject query = queryBuilder.get();
        
        QueryCriteria queryCriteria = new QueryCriteria(DatabaseUtil.getCollection("people", "foo"));
        queryCriteria.setQuery(query).setExplain(true);
        
        //display with no hint
        System.out.println(queryCriteria.submitFind());
        
        //add a hint for document {a:1}
        queryCriteria.setHint(new BasicDBObject("a", 1));
        System.out.println(queryCriteria.submitFind());
        
        //use the string name for the index
        queryCriteria.setHintString("b_1");
        System.out.println(queryCriteria.submitFind());
    }
}