FROM yonggyo00/ubuntu
ARG JAR_FILE=build/libs/loanservice-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=default,ml
ENV DB_HOST=localhost:1521
ENV DDL_AUTO=update

ENTRYPOINT ["java", "-jar", "-Dconfig.server=${CONFIG_SERVER}", "-Ddb.host=${DB_HOST}", "-Ddb.username=${DB_USERNAME}", "-Ddb.password=${DB_PASSWORD}", "-Dddl.auto=${DDL_AUTO}", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dpython.run.path=${PYTHON_RUN_PATH}", "-Dpython.script.path=${PYTHON_SCRIPT_PATH}", "-Deureka.server=${EUREKA_SERVER}", "-Dhostname=${HOSTNAME}", "app.jar"]

EXPOSE 3009