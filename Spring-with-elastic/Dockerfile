FROM openjdk:8-jdk-alpine
COPY pom.xml /home/pom.xml
COPY src /home/src
CMD["cd","/home/"]
CMD["mvn","clean","install"]
EXPOSE 1010/tcp
CMD["java","-jar","./target/api_insert-0.0.1-SNAPSHOT.jar"]
