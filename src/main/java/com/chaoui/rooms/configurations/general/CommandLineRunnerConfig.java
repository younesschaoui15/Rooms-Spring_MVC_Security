package com.chaoui.rooms.configurations.general;

import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.utils.InitDatabaseSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

            var users = initDatabaseSchema.initUsers();

            var rooms = initDatabaseSchema.initRooms();

            var topics = initDatabaseSchema.initTopics(users.get(1).getId(), rooms.getFirst().getId());

            //Waiting 5 seconds
            final int waitFor = 2;
            System.out.println("> Waiting "+waitFor+" seconds...");
            TimeUnit.SECONDS.sleep(waitFor);

            if (!topics.isEmpty()) {
                IO.println("# Topic: "+ topics.getFirst());
                IO.println("# Room: "+ topics.getFirst().getRoom());
                IO.println("# User: "+ topics.getFirst().getUser());
            } else {
                IO.println("# No topics created");
            }
        };
    }

    void deleteUsers(List<User> users, InitDatabaseSchema initDatabaseSchema) {
        try {
            final int maxSecs = 5;
            System.out.println("> Waiting " + maxSecs + " seconds...");

            //Waiting countdown
            new Thread(() -> {
                int _maxSecs = maxSecs;
                while (_maxSecs > 0) {
                    try {
                        System.out.println("> [" + _maxSecs + "]");
                        _maxSecs--;
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            Thread.sleep(maxSecs * 1000);

            int userIndex = 1;
            User userToDelete = users.get(userIndex);
            System.out.println("# Deleting User: "+ userToDelete);
            var id = initDatabaseSchema.deleteUserById(userToDelete.getId());
            System.out.println("######### User deleted: " + id.orElse(null));
//                id = initDatabaseSchema.deleteUserByUsername(users.get(1).getCredentials().getUsername());
//                System.out.println("######### User deleted: " + id.orElse(null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
