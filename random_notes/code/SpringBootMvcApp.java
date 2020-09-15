@SpringBootApplication(scanBasePackages="my.package")
@EnableScheduling
public class MyApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MyApplication.class);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
