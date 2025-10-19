# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/firstlook-digital-1.0.0.jar app.jar
EXPOSE 5000
ENV PORT=5000
ENTRYPOINT ["java", "-Xmx512m", "-Dserver.port=${PORT}", "-jar", "app.jar"]
