# Build Stage
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copy backend files
COPY backend/pom.xml .
COPY backend/src ./src

# Copy frontend into Spring Boot static folder
COPY frontend ./src/main/resources/static

# Build jar
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17

WORKDIR /app

# Copy built jar
COPY --from=build /app/target/avva-home-foods-1.0.0.jar app.jar

# Render dynamically assigns PORT; expose it (Spring Boot reads ${PORT:8080})
EXPOSE ${PORT:-8080}

# Start app
ENTRYPOINT ["java", "-jar", "app.jar"]