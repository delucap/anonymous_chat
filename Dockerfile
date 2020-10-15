FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/delucap/pasquale_deluca_adc_20.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/pasquale_deluca_adc_20/AnonymousChatADC /app
RUN mvn package

FROM openjdk:8-jre-alpine
WORKDIR /app
ENV MASTERIP=127.0.0.1
ENV ID=0

COPY --from=1 /app/target/AnonymousChatADC-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app
CMD /usr/bin/java -jar AnonymousChatADC-0.0.1-SNAPSHOT-jar-with-dependencies.jar -m $MASTERIP -id $ID
