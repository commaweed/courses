package week.one.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloAllInOne {
	public static void configureHomePageRoute() {
		Spark.get(new Route("/") {
			@Override
			public Object handle(Request request, Response response) {
				String data = null;
				try {
					data = getTemplateData("hello.ftl");
				} catch (TemplateException | IOException e) {
					e.printStackTrace();
					halt(500);
				}

				return data;
			}
		});		
	}
	
	//DBObject is a map
	public static DBObject getDocument() throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
		DBCollection collection =  client.getDB("course").getCollection("hello");
		return collection.findOne();
	}
	
	public static String getTemplateData(
		String template
	) throws TemplateException, IOException {
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(HelloAllInOne.class, "/");
		
		Template helloTemplate = configuration.getTemplate(template);
		
		//DBO
		StringWriter writer = new StringWriter();
		helloTemplate.process(getDocument(), writer);
		
		return writer.toString();		
	}
	
	public static void main(String[] args) {
		configureHomePageRoute();
	}
}