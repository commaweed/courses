package week.one.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloSparkAndFreeMarker {
    
    public static void configureHomePageRoute() {
        //a route is a pattern that this route should match (home page in this case)
        Spark.get(new Route("/") {
            
            @Override
            public Object handle(Request request, Response response) {
                //data
                Map<String, Object> helloMap = new HashMap<>();
                helloMap.put("name", "travis");
                
                String data = null;
                try {
                    data = getTemplateData("hello.ftl", helloMap);
                } catch (TemplateException | IOException e) {
                    e.printStackTrace();
                    halt(500);
                }

                return data;
            }
        });     
    }
    
    public static String getTemplateData(
        String template,
        Map<String, Object> data
    ) throws TemplateException, IOException {
        //configure
        //in classpath relative to HelloFreeMarker class, start of classpath
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloFreeMarker.class, "/");
        
        //get a handle to the template
        Template helloTemplate = configuration.getTemplate(template);
        
        //merge the data with the template into a writer (simple string in this case)
        StringWriter writer = new StringWriter();
        helloTemplate.process(data, writer);
        
        return writer.toString();       
    }
    
    public static void main(String[] args) {
        configureHomePageRoute();
    }
}