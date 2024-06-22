FROM openjdk:21-oracle
COPY target/SpotifyLike-1.0.0.jar SpotifyLike-1.0.0.jar
ENTRYPOINT ["java", "-jar", "./SpotifyLike-1.0.0.jar"]