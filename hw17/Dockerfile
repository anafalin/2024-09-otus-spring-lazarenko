FROM bellsoft/liberica-openjdk-alpine-musl:17

LABEL maintainer="Anastasia Lazarenko <an.afalin@yandex.ru>"
LABEL description="This is a Docker image based on Debian with java and Library application."
LABEL version="2.0"
LABEL release-date="2025-02-10"

COPY /target/*.jar /library-app-hw17.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/library-app-hw17.jar"]