package com.chaoui.rooms.configurations.general;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.PathApiVersionResolver;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig {

    @Bean
    WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureApiVersioning(ApiVersionConfigurer configurer) {
                configurer.useVersionResolver(
                        new PathApiVersionResolver(
                            1,
                            requestPath -> requestPath
                                .pathWithinApplication()
                                .value()
                                .startsWith("/api/") // only versioned if REST API and under "/api/"
                        ))
                    .setDefaultVersion("1")
                    .setVersionRequired(false);
            }

            //This used to add the versioning prefix to REST controllers implicitly
//            @Override
//            public void configurePathMatch(PathMatchConfigurer configurer) {
//                //Only controllers with @RestController annotation
//                configurer.addPathPrefix("/api/{version}", c -> c.isAnnotationPresent(RestController.class));
//
//                //Only controllers in the com.chaoui.rooms.controllers.rest package
//                //configurer.addPathPrefix("/{v}", c -> c.getPackage().getName().equals("com/chaoui/rooms/controllers/rest"));
//            }
        };
    }

}
