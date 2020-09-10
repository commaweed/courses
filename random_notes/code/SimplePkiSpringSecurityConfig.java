/**
* Spring application context config file for everything related to spring security that uses a self-signed cert.
*/
@Configuration
@EnableWebSecurity
//@Profile("simple-pki")
@PropertySource( { WebAppInitializer.PROPERTY_FILE_SOURCE } ) 
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "blah.components") // moved from WebAppConfig so can see services (this is parent context of WebAppConfig)
public class SimplePkiSpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;
    
    // component scan makes this available
    @Autowired
    private UserDetailsService userDetailsService;
    
    // url patterns to ignore 
    private static String[] IGNORED_REQUEST_PATTERNS = new String[] {
        String.format("/**/views/%s/**", WebAppConfig.VIEWS_ERROR_DIRECTORY),
        "/401",
        "/403",
        "/404",
        "/500",
        "/resources/**",
        "/*.js",
        "/*.css"
        // "app.bundle.js"
    };
    
    /**
    * cause spring security to avoid applying itself to certain requests
    * <pre>(i.e. same as <http pattern="/blah/**" security="none" />)</pre>
    */
    @Override
    public void configure(WebSecurity security) throws Exception {
        security.ignoring().antMatchers(IGNORED_REQUEST_PATTERNS);
    }
    
    @Override
    protected void configure(HttpSecurity http) {
        final String NAME_UUID_REGEX = "CN=(.*?)(?:,|$)";
        
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().accessDenied("/401")
            .and()
            .authorizeRequests()
            .filterSecurityInterceptorOncePerRequest(false)
            .anyRequest().authenticated()
            .and()
            .x509().subjectPrincipalRegex(NAME_UUID_REGEX).userDetailsService(userDetailsService);
            
            // .and()
            // .requiresChannel()
            // .antMatchers("/**").requiresSecure();
    }
}
