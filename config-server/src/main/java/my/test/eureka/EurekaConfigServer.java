package my.test.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@SpringBootApplication
@EnableConfigServer
public class EurekaConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(EurekaConfigServer.class, args);
	}

}
