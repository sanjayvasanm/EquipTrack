package com.equiptrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableJpaRepositories("com.equiptrack.repository")
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
public class EquipTrackApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EquipTrackApplication.class, args);
        Environment env = ctx.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║    EquipTrack System Started Successfully!    ║");
        System.out.printf ("║    Access at: http://localhost:%-5s        ║%n", port);
        System.out.println("╚════════════════════════════════════════════════╝");
    }
}
