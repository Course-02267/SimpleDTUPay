FROM eclipse-temurin:21 as jre-build
RUN mkdir -p /usr/src/dtupay
COPY target/simple-dtu-pay-1.0-SNAPSHOT.jar /usr/src/dtupay
WORKDIR /usr/src/dtupay
CMD java -Xmx64m \
-cp simple-dtu-pay-1.0-SNAPSHOT.jar dtu.simplepay.Main
