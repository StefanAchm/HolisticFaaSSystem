# Use a base image with JDK 11 (or the version you are using)
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built Spring Boot jar file into the container
COPY build/libs/holistic-faas-management-system-0.0.1-SNAPSHOT.jar app.jar

# Copy the logback.xml file into the container
COPY /logback.xml logback.xml

# Expose the port that Spring Boot runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]