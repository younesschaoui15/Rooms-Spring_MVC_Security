package com.chaoui.rooms.configurations.general;

import com.chaoui.rooms.entities.Room;
import com.chaoui.rooms.entities.User;
import com.chaoui.rooms.services.RoomService;
import com.chaoui.rooms.utils.InitDatabaseSchema;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class CommandLineRunnerConfig {

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Bean
    public CommandLineRunner commandLineRunner(InitDatabaseSchema initDatabaseSchema,
                                               RoomService roomService) {
        return args -> {
            System.out.println("""
                ##########################################################################
                #################### Application Started Successfully ####################
                ##########################################################################
                """);

            if (hibernateDdlAuto.equals("create")) {
                var users = initDatabaseSchema.initUsers();

                var rooms = initDatabaseSchema.initRooms();

                User developer = users.get(1);
                User manager = users.get(3);
                Room roomIT = rooms.getFirst();
                Room roomManagers = rooms.get(1);

                var topics = initDatabaseSchema.initTopics(developer.getId(), roomIT.getId());
                var topics2 = initDatabaseSchema.initTopics(manager.getId(), roomManagers.getId());

                //Waiting 3 seconds
                waitPlease(5);

                if (!topics.isEmpty()) {
//                topics.forEach(topic -> {
//                    System.out.println("Topic: " + topic.toString());
//                    IO.println("# Replies: "+ topic.getReplies());
//                });
//                IO.println("# Topic: "+ topics.getFirst());
//                IO.println("# Room: "+ topics.getFirst().getRoom());
//                IO.println("# User: "+ topics.getFirst().getUser());

//                    if (!rooms.isEmpty()) {
//                        Room r = rooms.getFirst();
//                        IO.println("# Room 1: " + r);
//                        IO.println("# Room 1 topics: " + roomService.getTopics(r.getId()).size());
//                    }
                    IO.println("# Done!");
                } else {
                    IO.println("# No topics created");
                }
            }

        };
    }

    private static void waitPlease(int seconds) {
        System.out.println("\n> Waiting " + seconds + " seconds...");
        AtomicInteger countdown = new AtomicInteger(seconds);

        CompletableFuture.runAsync(() -> {
            while (countdown.get() > 0) {
                try {
                    IO.print("[" + countdown.get() + "]  ");
                    countdown.decrementAndGet();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            TimeUnit.SECONDS.sleep(seconds);
            IO.println("\n");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
