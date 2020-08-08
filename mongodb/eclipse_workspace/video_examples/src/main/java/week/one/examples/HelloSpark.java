package week.one.examples;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

//default port is 4567
public class HelloSpark {
    public static void main(String[] args) {
        //a route is a pattern that this route should match (home page in this case)
        Spark.get(new Route("/") {
            
            @Override
            public Object handle(Request arg0, Response arg1) {
                //return the following response
                return "Hello World From Spark";
            }
        });
    }
}