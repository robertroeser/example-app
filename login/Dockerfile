FROM adoptopenjdk/openjdk11:x86_64-ubuntu-jdk-11.28
MAINTAINER Netifi Proteus Team
ARG APP_VERSION
ENV APP_VERSION ${APP_VERSION}
VOLUME /tmp
ADD build/distributions/login-boot-${APP_VERSION}.tar /opt/
ENTRYPOINT ["sh","-c","/opt/login-boot-${APP_VERSION}/bin/login", "-fg"]

