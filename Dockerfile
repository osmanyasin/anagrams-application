# Use a lightweight JDK 21 image as the base
FROM alpine/java:21-jdk

# Set the working directory in the image
WORKDIR /app

# Copy the application JAR file into the image
COPY target/anagrams-application-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application listens on
EXPOSE 8080

# Command to run when the container starts
CMD ["java", "-jar", "app.jar"]