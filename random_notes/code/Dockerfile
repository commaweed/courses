FROM repo/java-centos-openjdk8-jdk

EXPOSE 8096

VOLUME [ "/usr/src/service/config", "/etc/pki/mypki.jks" ]

WORKDIR /usr/src/service
COPY ./target/*.jar my-service.jar
COPY ./src/main/resources/* ./config/
CMD [ "java" ,"-jar", "my-service.jar" ]

====================================
docker-compose.yml
====================================

my-service:
    image: my-service:latest
    hostname: my-service
    domainname: my-service
    stdin_open: true
    tty: true
    ports:
        - "8096:8096"
    environment:
        - spring.profiles.active=myprofile
    volumes:
        - /service/application.properties:/usr/src/service/config/application.properties
        - /service/mypki.jks:/etc/pki/mypki.jks
