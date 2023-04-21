package my.test.eureka.config;

import java.util.Properties;

public interface KafkaConfiguration {
    Properties generateKafkaProperties();

    String getLogonTopicName();
}
