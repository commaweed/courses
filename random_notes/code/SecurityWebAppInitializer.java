/**
* Registers the security configuration class with web.xml.  That is, it will automatically register the springSecurityFilterChain filter 
* for every URL in the application. If there aren't any configuration changes for the filter, no additional code is necessary.
*/
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer { 
}
