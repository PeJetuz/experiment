package my.test.eureka;

import my.test.eureka.config.KafkaConfiguration;
import my.test.eureka.config.KafkaConfigurationImpl;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootTest(
        properties = {"spring.config.use-legacy-processing=true", "spring.cloud.config.enabled:false"},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SpringStarter.class)
@AutoConfigureMockMvc
class SpringStarterTests {

    @TestConfiguration
    static class SpringStarterTestsContextConfiguration {
        @Bean
        public KafkaConfiguration kafkaConfiguration() {
            return new KafkaConfigurationImpl();
        }
    }

    @Test
    void contextLoads() {
    }

}
