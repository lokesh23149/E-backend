FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY mvnw ./
COPY .mvn ./.mvn
COPY pom.xml ./

# 🔥 BULLETPROOF FIX
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw -B -q dependency:go-offline

COPY src ./src

RUN ./mvnw -B -DskipTests clean package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
