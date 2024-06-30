FROM openjdk:21-oracle
COPY target/spotify-like-1.0.0.jar spotify-like-1.0.0.jar
ENTRYPOINT ["java", "-jar", "./spotify-like-1.0.0.jar"]