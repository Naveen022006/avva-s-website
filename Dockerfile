# Build Stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the backend project files
COPY backend/pom.xml .
COPY backend/src ./src

# Copy the frontend files to the static resources directory of the backend
# This allows Spring Boot to serve the frontend at the root URL
COPY frontend ./src/main/resources/static

# Build the application with optimized settings
RUN mvn clean package -DskipTests -q

# Run Stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Set environment variables for production
ENV JAVA_OPTS="-Xmx384m -Xms128m"
ENV SPRING_PROFILES_ACTIVE=prod

# Copy the built JAR from the build stage
COPY --from=build /app/target/avva-home-foods-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Health check for Render
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Run the application
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
