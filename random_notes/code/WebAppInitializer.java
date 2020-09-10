/**
* Servlet 3.0 engine allows us to replace web.xml with this file (i.e. java configuration).
* AbstractAnnotationConfigDispatcherServletInitializer contains boiler plate code that simulates Spring dispatcher servlet creation in web.xml
* <pre>
* Directory structure:
* business
* config
* components
*    controllers
*    repositories
*    services
*       security
* exception
* </pre>
*/
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static final String PROPERTY_FILE_SOURCE = "classpath:application.properties";
    
    @Override
    public void onStartup(ServletContext context) throws ServletException {
        super.onStartup(context);
    }
    
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
            new CharacterEncodingFilter("UTF-8", true, true)
        };
    }
    
    /**
    * Register all of the configuration classes with the root web application context.
    */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
            SimplePkiSpringSecurityConfig.class,
            ElasticSearchConfig.class
        };
    }
    
    /**
    * Register web-related config files in the "dispatcher" servlet application context (Spring Beans)
    */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
            WebAppConfig.class
        };
    }
    
    /**
    * Any class you register here will have a chance to set some spring things before the enitre spring application context is created.
    */
    @Override
    protected ApplicationContextInitializer<?>[] getRootApplicationContextInitializers() {
        return new ApplicationContextInitializer[] {
            new MyApplicationContextInitializer()
        };
    }
    
    /**
    * The "dispatcher" servlet mapping URLs.
    */
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
}
