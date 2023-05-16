ARG BASE_IMAGE=dragonwell-registry.cn-hangzhou.cr.aliyuncs.com/dragonwell/dragonwell:8-extended-ga-anolis
FROM $BASE_IMAGE
LABEL org.opencontainers.image.authors="yanchuan"

ARG JAR_FULL_PATH=target/sentinel-dashboard.jar
ENV JAVA_OPTS=-Xmx2g

WORKDIR /opt/deploy/
COPY $JAR_FULL_PATH app.jar
CMD java $JAVA_OPTS $CATALINA_OPTS -jar app.jar