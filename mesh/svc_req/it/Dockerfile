
# 1st stage, build the app
FROM container-registry.oracle.com/java/openjdk:21 as build

# Install maven
WORKDIR /usr/share
RUN set -x && \
    curl -O https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xvf apache-maven-*-bin.tar.gz  && \
    rm apache-maven-*-bin.tar.gz && \
    mv apache-maven-* maven && \
    ln -s /usr/share/maven/bin/mvn /bin/

WORKDIR /test

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD src src
ADD pom.xml pom.xml

RUN mvn install

RUN echo "done!"

# 2nd stage, build the runtime image
FROM container-registry.oracle.com/java/openjdk:21
WORKDIR /test

# Copy the binary built in the 1st stage
COPY --from=build /test/target/header_test.jar ./
COPY --from=build /test/target/libs ./libs

CMD ["java", "-jar", "header_test.jar"]

EXPOSE 8080
