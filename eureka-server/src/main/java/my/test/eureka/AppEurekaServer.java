package my.test.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Spring application runner.
 * Hello world!
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class AppEurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(AppEurekaServer.class, args);
    }
}
