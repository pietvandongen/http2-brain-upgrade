FROM java:8-jre
EXPOSE 8080
COPY build/libs/server.jar /usr/bin/
COPY keystore.jks /usr/bin/
CMD cd /usr/bin && java -jar server.jar
