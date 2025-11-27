FROM openjdk:17.0.1-jdk-buster
COPY target/fxdeal-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8086
CMD ["java", "-jar", "/app.jar"]
