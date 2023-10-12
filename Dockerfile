FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Games-0.0.1-SNAPSHOT.jar resultado.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","resultado.jar"]
