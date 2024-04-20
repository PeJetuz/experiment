# 1st stage, build the app
FROM ghcr.io/graalvm/graalvm-community:21.0.0-ol9 as build

WORKDIR /usr/share

# Install maven
RUN set -x && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xvf apache-maven-*-bin.tar.gz  && \
    rm apache-maven-*-bin.tar.gz && \
    mv apache-maven-* maven && \
    ln -s /usr/share/maven/bin/mvn /bin/

WORKDIR /service_bus

ADD pom.xml pom.xml
ADD src src

# RUN mvn test

CMD ["mvn", "test"]
