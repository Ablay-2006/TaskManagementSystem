# Multi-stage build
# Stage 1: Builder
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy jar from builder
COPY --from=builder /build/target/*.jar app.jar

# Create non-root user
RUN chown -R spring:spring /app

# Create necessary directories
RUN mkdir -p /app/uploads /app/logs && chown -R spring:spring /app/uploads /app/logs

USER spring

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget -q -O - http://localhost:8080/actuator/health || exit 1

# Spring Boot app entrypoint with JVM optimization
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]

