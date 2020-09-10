/**
* java configuration for all things related to elasticsearch
*/
@Configuration
public class ElasticSearchConfig {
    
    @Autowired
    private Environment env;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchConfig.class);
    
    @Bean
    public TransportClient getElasticSearchClient() {
        String host = env.getRequiredProperty("host");
        String clusterName = env.getRequiredProperty("clusterName", "default-value");
        int port = env.getRequiredProperty("port", Integer.class);
        
        LOGGER.info("creating client with host=[{}], port=[{}]", host, port);
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            LOGGER.error("blah", e);
        }
        return client;
    }
}
