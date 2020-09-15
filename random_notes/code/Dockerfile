FROM repo/java-centos-openjdk8-jdk

EXPOSE 8096

VOLUME [ "/usr/src/service/config", "/etc/pki/mypki.jks" ]

WORKDIR /usr/src/service
COPY ./target/*.jar my-service.jar
COPY ./src/main/resources/* ./config/
CMD [ "java" ,"-jar", "my-service.jar" ]
