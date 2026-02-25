# Build JAR
FROM maven:3.9.9-amazoncorretto-21 AS build
COPY src /tmp/src/
COPY pom.xml /tmp/
WORKDIR /tmp/
RUN mvn clean install

# Execute JAR
FROM amazoncorretto:21.0.9
COPY --from=build /tmp/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]