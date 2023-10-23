FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/cakemanagerservice-1.0.0.jar .
CMD ["java", "-jar", "cakemanagerservice-1.0.0.jar"]
