# === Build stage ===
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

COPY pom.xml ./
COPY /src /src

RUN mvn clean package -DskipTests

# === Run stage ===

FROM eclipse-temurin:21.0.5_11-jre-alpine-3.21

COPY --from=builder /target/*.jar ./app.jar

EXPOSE 9090

CMD ["java", "-jar", "app.jar"]