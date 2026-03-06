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

# Run Stage - eclipse-temurin has curl/wget pre-installed (amazoncorretto does NOT)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Set JVM memory limits for Render's 512MB free tier
ENV JAVA_OPTS="-Xmx384m -Xms128m"

# Copy the built JAR from the build stage
COPY --from=build /app/target/avva-home-foods-1.0.0.jar app.jar

# Expose the application port
EXPOSE 8080

# Health check for Render — uses wget (available in eclipse-temurin:17-jre-jammy)
# start-period=90s to account for free-tier cold start + Maven JAR startup time
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=5 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Run the application (sh -c ensures $JAVA_OPTS shell variable expands correctly)
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
