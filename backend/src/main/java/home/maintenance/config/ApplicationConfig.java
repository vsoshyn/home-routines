package home.maintenance.config;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Created by vsoshyn on 28/10/2016.
 */
@Configuration
@EnableWebMvc
@Import({PersistenceConfig.class, StaticResourcesConfig.class, SecurityConfig.class})
@ComponentScan(basePackages = {"home.maintenance.controller", "home.maintenance.service"})
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        return new InternalResourceViewResolver();
    }

}
