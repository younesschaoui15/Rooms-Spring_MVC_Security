package com.chaoui.rooms.configurations.general;

import com.chaoui.rooms.utils.InitDatabaseSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineRunnerConfig {

    @Bean
    public CommandLineRunner commandLineRunner(InitDatabaseSchema initDatabaseSchema) {
        return args -> {
            System.out.println("""
                ##########################################################################
                #################### Application Started Successfully ####################
                ##########################################################################
                """);

            initDatabaseSchema.initUsers();
        };
    }
}
