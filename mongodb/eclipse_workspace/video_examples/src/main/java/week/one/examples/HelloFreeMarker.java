package week.one.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloFreeMarker {
    public static void main(String[] args) throws IOException, TemplateException {
        //configure
        //in classpath relative to HelloFreeMarker class, start of classpath
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloFreeMarker.class, "/");
        
        //get a handle to the template
        Template helloTemplate = configuration.getTemplate("hello.ftl");
        
        //set up the name-value pair data
        Map<String, Object> helloMap = new HashMap<>();
        helloMap.put("name", "FreeMarker");
        
        //merge the data with the template into a writer (simple string in this case)
        StringWriter writer = new StringWriter();
        helloTemplate.process(helloMap, writer);
        
        //print the results
        System.out.println(writer.toString());
    }
}