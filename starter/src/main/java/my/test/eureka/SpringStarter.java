package my.test.eureka;

import java.util.Arrays;
import my.test.authorization.domain.impl.PolicyBuilderImpl;
import my.test.authorization.servicebus.LogonEventTransmitterBuilderImpl;
import my.test.authorization.store.UserMockBuilderImpl;
import my.test.eureka.config.KafkaConfiguration;
import my.test.rest.incomings.controllers.AuthenticationController;
import my.test.rest.incomings.controllers.api.dto.Authentication;
import my.test.rest.incomings.controllers.api.dto.Token;
import my.test.authorization.rules.AuthFactory;
import my.test.authorization.rules.AuthFactoryImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@SpringBootApplication
@ComponentScan
public class SpringStarter {

    public static void main(String[] args) {
        SpringApplication.run(SpringStarter.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    //	@Bean
//	public PolicyBuilder policyBuilder() {
//		return new PolicyBuilderImpl(new AuthStoreMock());
//	}
    @Bean
    public AuthFactory authFactory(KafkaConfiguration kafkaConfiguration) {
        return new AuthFactoryImpl(
                new PolicyBuilderImpl(new UserMockBuilderImpl(),
                        new LogonEventTransmitterBuilderImpl(kafkaConfiguration.generateKafkaProperties(),
                                kafkaConfiguration.getLogonTopicName())));
    }

    //@Bean
    public AuthenticationController authenticationsController(AuthFactory authFactory,
            AutowireCapableBeanFactory beanFactory, GenericWebApplicationContext webappContext,
            @Value("${service1.authenticationsControllerUri}") final String authenticationsControllerUri) {
        final String urlPath = "/api";

        @Controller
        @RequestMapping("/api"/*"/${service1.authenticationsControllerUri}"*/)
        class AuthenticationsControllerBean extends AuthenticationController {

            public AuthenticationsControllerBean(AuthFactory authenticationInteractor) {
                super(authFactory);
            }

            public ResponseEntity<Authentication> login() {
                return ResponseEntity.ok(
                        new Authentication().accessToken(new Token().value("login spring")).username("test spring"));
            }
        }

        AuthenticationsControllerBean authenticationsControllerBean = new AuthenticationsControllerBean(authFactory);
        beanFactory.autowireBean(authenticationsControllerBean);

        webappContext.getBeanFactory().registerSingleton("authenticationsController", authenticationsControllerBean);
        webappContext.getBeansOfType(RequestMappingHandlerMapping.class).forEach((k, v) -> v.afterPropertiesSet());
//		webappContext.getBeansOfType(RequestMappingHandlerMapping.class).forEach((name, requestMappingHandlerMapping) -> {
//			requestMappingHandlerMapping.getHandlerMethods().keySet().forEach(requestMappingHandlerMapping::unregisterMapping);
//			requestMappingHandlerMapping.afterPropertiesSet();
//		});

        //https://stackoverflow.com/questions/5758504/is-it-possible-to-dynamically-set-requestmappings-in-spring-mvc
//		RequestMappingInfo requestMappingInfo = RequestMappingInfo
//				.paths(urlPath)
//				.methods(RequestMethod.GET)
//				.produces(MediaType.APPLICATION_JSON_VALUE)
//				.build();
//		requestMappingHandlerMapping.
//				registerMapping(requestMappingInfo, authenticationsControllerBean,
//						AuthenticationsControllerBean.class.getDeclaredMethod("handleRequests")
//				);
        return authenticationsControllerBean;
    }

}
