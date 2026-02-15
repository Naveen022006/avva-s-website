# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY backend/pom.xml .
COPY backend/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
# Copy frontend for static serving (if backend configured to serve it)
# Ideally, frontend should be served by Nginx or built into Spring Boot via resources
# For this structure, we assume backend might need access to it or we are just packaging it.
COPY frontend /app/frontend
ENTRYPOINT ["java","-jar","/app.jar"]
