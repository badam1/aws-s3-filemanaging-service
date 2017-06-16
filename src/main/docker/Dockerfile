FROM java:8-jre
EXPOSE 6666
ADD amazon_s3_fileservice-0.0.1-SNAPSHOT.jar amazon_s3_fileservice.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar","/amazon_s3_fileservice.jar"]
