# -------- Build stage --------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy Maven config and source code
COPY pom.xml .
COPY src ./src

# Build the JAR (no tests)
RUN mvn -q -DskipTests clean package

# -------- Run stage --------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Spring Boot default port
EXPOSE 8080

# Start the app
ENTRYPOINT ["java","-jar","/app.jar"]
