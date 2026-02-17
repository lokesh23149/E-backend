# Multi-stage build for Spring Boot backend
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Maven wrapper + pom first for better caching
COPY mvnw ./
COPY .mvn ./.mvn
COPY pom.xml ./

#  CRITICAL FIX
RUN chmod +x mvnw

# Download dependencies (cached)
RUN ./mvnw -B -q dependency:go-offline

# Copy source
COPY src ./src

# Build jar
RUN ./mvnw -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]
