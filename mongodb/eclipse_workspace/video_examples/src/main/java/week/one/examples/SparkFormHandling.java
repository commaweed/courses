package week.one.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SparkFormHandling {
	public static String getTemplateData(
		String template,
		Map<String, Object> data
	) throws TemplateException, IOException {
		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(SparkFormHandling.class, "/");
		
		Template helloTemplate = configuration.getTemplate(template);
		
		//DBObject is a map and freemarker takes a map
		StringWriter writer = new StringWriter();
		helloTemplate.process(data, writer);
		
		return writer.toString();		
	}	
	
	public static void main(String[] args) {
		Spark.get(new Route("/") {
			@Override
			public Object handle(Request request, Response response) {
				Map<String, Object> fruits = new HashMap<>();
				fruits.put("fruits", Arrays.asList("apple", "pear", "banana", "peach"));
				
				String data = null;
				try {
					data=getTemplateData("fruit_picker.ftl", fruits);
				} catch (TemplateException | IOException e) {
					halt(500);
					e.printStackTrace();
				}
				
				return data;
			}
		});
		
		Spark.post(new Route("/favorite_fruit") {
			@Override
			public Object handle(Request request, Response response) {
				String fruit = request.queryParams("fruit");
				return fruit == null ? "Enter something" : "you picked ["+fruit+"]";
			}
		});

	}
}