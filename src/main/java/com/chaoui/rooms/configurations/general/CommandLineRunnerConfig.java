package com.chaoui.rooms.configurations.general;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineRunnerConfig {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("#################### Application Started Successfully ####################");
        };
    }
}
