#
# Build stage
#
FROM maven:3.6.3-openjdk-15 AS build
COPY src /home/app/src
#COPY src/main/resources/DockerApplication.properties /home/app/src/main/resources/application.properties
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:15-jdk-buster
RUN apt-get update
COPY --from=build /home/app/target/Note-app-0.0.1-SNAPSHOT.jar /usr/local/lib/sql.jar
COPY --from=build /home/app/target/classes /usr/local/lib/
WORKDIR /usr/local/lib/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/sql.jar"]
