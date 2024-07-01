FROM openjdk:21-oracle
EXPOSE 8080
COPY target/spotify-like-1.0.0.jar spotify-like-1.0.0.jar
ENTRYPOINT ["java", "-jar", "./spotify-like-1.0.0.jar", "--server.port=8080"]