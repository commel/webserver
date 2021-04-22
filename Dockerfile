FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080/tcp
COPY *.class /
CMD ["java", "-cp", ".", "SmallJavaWebServer"]
