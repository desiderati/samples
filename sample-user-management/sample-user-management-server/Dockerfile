FROM api.repoflow.io/desiderati/docker/library/kotlin:21

COPY ./build/libs/sample-user-management-server*.jar /opt/kotlin-app/app.jar
COPY logback.xml /opt/kotlin-app/logback.xml

# Configures the supervisor.
COPY supervisor.ini /etc/supervisor/conf.d/supervisor.conf

ENTRYPOINT ["/opt/start-supervisor.sh"]
