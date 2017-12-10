package home.maintenance.config;

import home.maintenance.service.ImageRepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by Buibi on 22.01.2017.
 */
@EnableWebMvc
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${application.resources.static-locations}")
    private String staticResourcesLocation;
    @Autowired
    private ImageRepositoryManager imageRepository;
    @Value("${application.resources.static.drop-user-data}")
    private boolean dropUserData;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/**").addResourceLocations("file:/" + staticResourcesLocation);
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        if (dropUserData) {
            imageRepository.drop();
        }
    }

}