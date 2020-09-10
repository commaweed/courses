/**
* A spring application context initializer that will set the active spring profile according to the value in the property file.
* It will also add teh property file to environment (this is one way to do it) so it will be available here.
*/
public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MyApplicationContextInitializer.class);
    
    @Override
    public void initialize(ConfigurableApplicationContext context) {
        ConfigurableEnvironment env = context.getEnvironment();
        try {
        // add the property file to the environment as a source
        env.getPropertySources().addFirst(new ResourcePropertySource(WebAppInitializer.PROPERTY_FILE_SOURCE));
        
        // set the active spring profile to the property file value
        env.setActiveProfiles(env.getRequiredProperty("spring.profiles.active"));
        LOGGER.info("Spring active profile: {}", Arrays.toString(env.getActiveProfiles()));
        } catch (IOException e) {
            LOGGER.error("blah {}", WebAppInitializer.PROPERTY_FILE_SOURCE);
        }
    }
}
