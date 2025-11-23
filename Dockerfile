# -------- Build stage: JDK 21 + Maven --------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy Maven config and source files
COPY pom.xml .
COPY src ./src

# Build the JAR (no tests)
RUN mvn -q -DskipTests clean package

# -------- Run stage: JDK 21 only --------
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
