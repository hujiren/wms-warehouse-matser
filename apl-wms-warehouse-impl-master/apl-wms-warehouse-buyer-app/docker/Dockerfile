FROM openjdk:8-jdk-alpine
EXPOSE 9153
VOLUME /tmp
ENV jarFile /apl-wms-warehouse-buyer-app.jar
ADD $jarFile  $jarFile
#ENTRYPOINT [ "java", "-jar", "/jarFile" ]
ENTRYPOINT command java -jar $jarFile