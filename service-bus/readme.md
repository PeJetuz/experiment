run:

docker network create app-tier --driver bridge
docker run -d --name kafka-server2 --hostname kafka-server2 -p 9092:9092 -p 9093:9093 -p 9094:9094 \
--network app-tier \
-e KAFKA_CFG_NODE_ID=0 \
-e KAFKA_CFG_PROCESS_ROLES=controller,broker \
-e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://0.0.0.0:9094 \
-e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka-server2:9092,EXTERNAL://localhost:9094 \
-e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT \
-e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka-server2:9093 \
-e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER \
bitnami/kafka:3.9.0

# https://hub.docker.com/r/bitnami/schema-registry
docker run -d --name schema-registry -p 8081:8081 \
--network app-tier \
--env SCHEMA_REGISTRY_DEBUG=true \
-e SCHEMA_REGISTRY_KAFKA_BROKERS=PLAINTEXT://kafka-server2:9092 \
-e SCHEMA_REGISTRY_LISTENERS=http://0.0.0.0:8081 \
bitnami/schema-registry:7.8.0

check topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic messaging-test-topic-snappy-compressed --from-beginning
