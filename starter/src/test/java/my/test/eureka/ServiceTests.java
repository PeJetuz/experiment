package my.test.eureka;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {"spring.config.use-legacy-processing=true", "spring.cloud.config.enabled:false"},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ServiceStarter.class)
@AutoConfigureMockMvc
class ServiceTests {

    @Test
    void contextLoads() {
    }

}
