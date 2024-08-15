# FROM openjdk:17-jdk
# VOLUME  /tmp
# #WORKDIR /app
# COPY target/*.jar  app.jar
# EXPOSE 8082
# ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1: Build the JAR file
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR file
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/stock
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
EXPOSE 8082
ENTRYPOINT ["java","-jar","app.jar"]
