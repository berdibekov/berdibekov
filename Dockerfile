#
# Build stage
#
FROM openjdk:14 AS build
COPY src /home/app/src
COPY pom.xml /home/app
COPY make.sh /home/app
COPY mvnw /home/app
COPY mvnw.cmd /home/app
COPY .mvn /home/app/.mvn
COPY scripts /home/app/scripts
#WORKDIR /home/app/
RUN chmod +x /home/app/make.sh
#RUN ./make.sh
CMD [ "/bin/bash", "/make.sh" ]
#
# Package stage
#
FROM openjdk:14
COPY --from=build /home/app/target/Poll-app-0.0.1-SNAPSHOT.jar /usr/local/lib/Poll-app-0.0.1-SNAPSHOT.jar
WORKDIR /usr/local/lib/
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/Poll-app-0.0.1-SNAPSHOT.jar"]
