FROM eclipse-temurin:21 as jre-build
RUN mkdir -p /usr/src/dtupay
COPY target/*.jar /usr/src/dtupay
WORKDIR /usr/src/dtupay
CMD java -Xmx64m \
-jar *.jar
