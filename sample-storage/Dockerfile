FROM openjdk:11-jdk

# Set the locale(en_US.UTF-8)
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en

# Setup gosu for easier command execution.
ENV GOSU_VERSION 1.11
RUN set -eux \
 && savedAptMark="$(apt-mark showmanual)" \
 && apt-get update \
 && apt-get install -y --no-install-recommends ca-certificates wget \
 && if ! command -v gpg; then \
      apt-get install -y --no-install-recommends gnupg2 dirmngr; \
    fi \
 && rm -rf /var/lib/apt/lists/* \
 && dpkgArch="$(dpkg --print-architecture | awk -F- '{ print $NF }')" \
 && wget -O /usr/local/bin/gosu "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch" \
 && wget -O /usr/local/bin/gosu.asc "https://github.com/tianon/gosu/releases/download/$GOSU_VERSION/gosu-$dpkgArch.asc" \
 && export GNUPGHOME="$(mktemp -d)" \
 && gpg --batch --keyserver ha.pool.sks-keyservers.net --recv-keys B42F6819007F00F88E364FD4036A9C25BF357DD4 \
 && gpg --batch --verify /usr/local/bin/gosu.asc /usr/local/bin/gosu \
 && command -v gpgconf && gpgconf --kill all || : \
 && rm -rf "$GNUPGHOME" /usr/local/bin/gosu.asc \
 && apt-mark auto '.*' > /dev/null \
 && [ -z "$savedAptMark" ] || apt-mark manual $savedAptMark \
 && apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false \
 && chmod +x /usr/local/bin/gosu \
 && gosu --version \
 && gosu nobody true

# Copy application.
RUN mkdir -p /opt/desiderati-java-app/
COPY target/sample-storage-0.0.1-SNAPSHOT-exec.war /opt/desiderati-java-app/app.jar

ENV JAVA_XMS 256m
ENV JAVA_XMX 512m
ENV JAVA_CPUS 1

COPY docker-entrypoint.sh /
RUN chmod +x /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]

WORKDIR /opt/desiderati-java-app/
EXPOSE 9090
CMD java $JAVA_OPTS -jar app.jar
