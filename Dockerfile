# Dockerfile
# Use a minimal base image with Java
FROM eclipse-temurin:17
#here defining dynamic jar file name
ARG APP_JAR_FILE
# 'maintainer' is the more common spelling
LABEL maintainer="tufailkarimkhan@gmail.com"

# Create and set the working directory inside the container
WORKDIR /app

# Copy your built Spring Boot JAR into that directory and rename it
COPY target/${APP_JAR_FILE} /app/app.jar

# Define the command to run your app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]