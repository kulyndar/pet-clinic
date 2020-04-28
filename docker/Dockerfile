FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG DOCKERIZE_VERSION

RUN wget -O dockerize.tar.gz https://github.com/jwilder/dockerize/releases/download/${DOCKERIZE_VERSION}/dockerize-alpine-linux-amd64-${DOCKERIZE_VERSION}.tar.gz \
 && tar xzf dockerize.tar.gz \
 && chmod +x dockerize
# if ARG is changed, cache is invalidated. So put changable ARG after download
ARG ARTIFACT_NAME
ARG EXPOSED_PORT
ENV SPRING_PROFILES_ACTIVE docker
ADD ${ARTIFACT_NAME}.jar /app.jar

EXPOSE ${EXPOSED_PORT}
ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
