FROM eclipse-temurin:21 as jre-build
WORKDIR /usr/src
COPY target/*.jar /usr/src/dtupay
CMD java -Xmx64m \
-jar quarkus-app/quarkus-run.jar
