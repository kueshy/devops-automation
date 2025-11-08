FROM maven:3.9.0-eclipse-temurin-17
EXPOSE 12080
ADD target/devops-integration.jar devops-integration.jar
ENTRYPOINT ["java", "-jar", "/devops-integration.jar"]