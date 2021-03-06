package local.ts3snet.unicbot_ms_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UnicBotGeSpringApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnicBotGeSpringApplication.class, args);
    }
}
