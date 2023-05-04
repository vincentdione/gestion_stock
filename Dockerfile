FROM openjdk:17-jdk
WORKDIR /app
COPY target/gestion-stock-0.0.1-SNAPSHOT.jar  /app/gestion-stock.jar
EXPOSE 8082
CMD ["java","-jar","gestion-stock.jar"]