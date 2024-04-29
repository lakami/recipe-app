package dev.pjatk.recipeapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    private static final String[] ALLOWED_STATIC_PATHS = {
        "/*.js",
        "/*.css",
        "/*.svg",
        "/*.png",
        "*.ico",
        "/index.html"
    };
    protected static final String CLASSPATH_LOCATION = "classpath:/static/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(ALLOWED_STATIC_PATHS).addResourceLocations(CLASSPATH_LOCATION);
    }

}
