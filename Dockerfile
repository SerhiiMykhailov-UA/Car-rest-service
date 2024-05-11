FROM openjdk:17-jdk-alpine
MAINTAINER baeldung.com
COPY car-rest-service-0.0.1-SNAPSHOT.jar car-rest-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/car-rest-service-0.0.1-SNAPSHOT.jar"]