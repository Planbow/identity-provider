# The Docker file
FROM openjdk:17-jdk-slim
MAINTAINER SUMIT CHOUKSEY "sumitchouksey2315@gmail.com"
COPY target/identity-provider.jar ./app.jar
EXPOSE 80
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
