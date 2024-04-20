/*
 * MIT License
 *
 * Copyright (c) 2023 Vladimir Shapkin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package my.test.eureka;

import java.util.Arrays;
import my.test.authorization.domain.api.servicebus.EventTransmitterFactory;
import my.test.authorization.domain.impl.PolicyFactoryImpl;
import my.test.authorization.rules.InteractorFactory;
import my.test.authorization.rules.impl.InteractorFactoryImpl;
import my.test.authorization.store.UserMockFactoryImpl;
import my.test.eureka.config.KafkaConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring application starter.
 *
 * @since 1.0
 * @checkstyle NonStaticMethodCheck (100 lines)
 */
@SpringBootApplication
@ComponentScan
public class SpringStarter {

    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static void main(final String[] args) {
        SpringApplication.run(SpringStarter.class, args);
    }

    /**
     * CommandLineRunner.
     *
     * @param ctx Application context.
     * @return CommandLineRunner.
     */
    @Bean
    @SuppressWarnings("PMD.SystemPrintln")
    public CommandLineRunner commandLineRunner(final ApplicationContext ctx) {
        return args -> {
            System.out.println("Let's inspect the beans provided by Spring Boot:");
            final String[] beans = ctx.getBeanDefinitionNames();
            Arrays.sort(beans);
            for (final String bean : beans) {
                System.out.println(bean);
            }
        };
    }

    /**
     * InteractorFactory.
     *
     * @param kconfig Kafka configuration.
     * @return InteractorFactory.
     */
    @Bean
    public InteractorFactory authFactory(final KafkaConfiguration kconfig) {
        return new InteractorFactoryImpl(
            new PolicyFactoryImpl(
                new UserMockFactoryImpl(),
                new EventTransmitterFactory.Stub()
            )
        );
    }
}
