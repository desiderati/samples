#!/bin/bash
set -e

if ! id -u java; then
  USER_ID=${LOCAL_USER_ID:-1000}
  echo "Adding 'java' user..."
  useradd -m -d /home/java -u ${USER_ID} -s /bin/bash java
  chown -R ${USER_ID}:${USER_ID} /opt
  echo "User 'java' added with success!"
fi

if [[ ${ENABLE_DEBUG} = true ]]; then
  echo "Enabling Debug Mode on Port: 8090"
  JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=8090,server=y,suspend=n"
fi

export JAVA_OPTS="$JAVA_OPTS -Xms$JAVA_XMS -Xmx$JAVA_XMX -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m
-Djava.net.preferIPv4Stack=true -XX:ParallelGCThreads=$JAVA_CPUS -XX:ConcGCThreads=$JAVA_CPUS
-Djava.util.concurrent.ForkJoinPool.common.parallelism=$JAVA_CPUS"

echo "Running Java App with following Options: $JAVA_OPTS"

exec gosu java "$@"
