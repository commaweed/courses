@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"blah.components"})
//@EnableAspectJAutoProxy(ProxyTargetClass = true)
public class WebAppConfig extends WebMvcConfigAdapter {
    
    public static final String VIEWS_ERROR_DIRECTORY = "error";
    
    @Autowired
    private Environment environment;
    
    /**
    * delegate any resource not handled by spring back to the container.
    */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configure.enable();
    }
    
    /**
    * Allow the controller layer to produce xml or json based upon a request parameter called "format" (e.g. format=json)
    */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .favorPathExtensions(false)
            .favorParameter(true)
            .defaultContentType(MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_XML);
    }
    
    /**
    * Override the xml and json message converters for application-specific configuration.
    */
    @Override
    public void configureMessageConverters(List<HttpMessageConverters<?>> converters) {
        converters.add(getJsonMessageConverter());
        converters.add(getXmlMessageConverter());
    }
    
    @Bean
    public MappingJackson2XmlHttpMessageConverter getXmlMessageConverter() {
        XmlMapper mapper = new XmlMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }
    
    @Bean
    public MappingJackson2HttpMessageConverter getJsonMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }
    
    /**
    * Adds a view resolver for XSLT transformation.
    */
    @Bean
    public ViewResolver getXsltViewResolver() {
        XsltViewResolver resolver = new XsltViewResolver();
        resolver.setOrder(1);
        resolver.setSourceKey("xmlSource");
        resolver.setCacheTemplates(true);
        resolver.setViewClass(XsltView.class);
        resolver.setPrefix("/WEB-INF/xsl/");
        resolver.setViewNames("xxEntry");
        resolver.setSuffix(".xsl");
        return resolver;
    }
    
    /**
    * Adds a view resolver for JSP pages.
    */
    @Bean
    public ViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setOrder(2);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    /**
    * Adds a bean name view resolver.
    */
    @Bean
    public ViewResolver getBeanNameViewResolver() {
        return new BeanNameViewResolver();
    }
    
    /**
    * Register url-to-view mappings.  Web.xml specifies the status code links.
    */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/401").setViewName(VIEWS_ERROR_DIRECTORY + "/unauthenticated");
        registry.addViewController("/403").setViewName(VIEWS_ERROR_DIRECTORY + "/unauthorized");
        registry.addViewController("/404").setViewName(VIEWS_ERROR_DIRECTORY + "/page_not_found");
        registry.addViewController("/500").setViewName(VIEWS_ERROR_DIRECTORY + "/error_occurred");
        registry.addViewController("/").setViewName("forward:/index.html");
    }
    
}
