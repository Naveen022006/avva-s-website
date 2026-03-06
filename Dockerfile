# =============================================================
# BUILD STAGE — Maven + JDK 17
# =============================================================
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# --- Dependency cache layer ---
# Copy pom.xml first and download all dependencies BEFORE copying source.
# Docker caches this layer and skips the 5-min download on subsequent builds
# unless pom.xml changes.
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# --- Copy source + frontend ---
COPY backend/src ./src
COPY frontend ./src/main/resources/static

# --- Build JAR (tests skipped, output visible for Render log debugging) ---
RUN mvn clean package -DskipTests

# =============================================================
# RUN STAGE — eclipse-temurin JRE (has wget pre-installed)
# =============================================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Memory limits tuned for Render free-tier 512 MB RAM
ENV JAVA_OPTS="-Xmx384m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Copy the built JAR from build stage
COPY --from=build /app/target/avva-home-foods-1.0.0.jar app.jar

# Render routes external traffic to the PORT it injects — expose that port
EXPOSE 8080

# Health check: Render also calls /actuator/health to verify the service is live.
# Uses PORT env var (default 8080) to probe the correct port.
# start-period=120s accounts for free-tier slow cold starts (Maven JARs are large).
HEALTHCHECK --interval=30s --timeout=15s --start-period=120s --retries=5 \
    CMD wget -qO- http://localhost:${PORT:-8080}/actuator/health || exit 1

# Start Spring Boot — sh -c is required so $JAVA_OPTS shell variable expands
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
