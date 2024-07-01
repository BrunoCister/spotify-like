FROM openjdk:21-oracle
EXPOSE 80
COPY target/spotify-like-1.0.0.jar spotify-like-1.0.0.jar
ENTRYPOINT ["java", "-jar", "./spotify-like-1.0.0.jar", "--server.port=80"]