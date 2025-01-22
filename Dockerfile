# use maven to build the application
FROM maven:3.9.9-amazoncorretto-21-al2023 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# use openjdk to run the application
FROM amazoncorretto:21-al2023
WORKDIR /app
COPY --from=build /app/target/scm-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]