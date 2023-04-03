#!/bin/sh
#
# Copyright (c) 2023 - Felipe Desiderati
#
# Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
# associated documentation files (the "Software"), to deal in the Software without restriction,
# including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
# and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
# subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all copies or substantial
# portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
# LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
# WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
# SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#

set -e

if ! id -u java >/dev/null 2>&1; then
  USER_ID=${LOCAL_USER_ID:-1000}
  echo "Adding 'java' user..."
  useradd -m -d /home/java -u "${USER_ID}" -s /bin/sh java
  chown -R "${USER_ID}":"${USER_ID}" /home/java
  chown -R "${USER_ID}":"${USER_ID}" /opt
  echo "User 'java' added with success!"
fi

if [ -n "${JAVA_XMS}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Xms${JAVA_XMS}"
fi

if [ -n "${JAVA_XMX}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Xmx${JAVA_XMX}"
fi

if [ -n "${JAVA_CPUS}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -XX:ParallelGCThreads=${JAVA_CPUS} -XX:ConcGCThreads=${JAVA_CPUS}"
  JAVA_OPTS="${JAVA_OPTS} -Djava.util.concurrent.ForkJoinPool.common.parallelism=${JAVA_CPUS}"
fi

JAVA_OPTS="${JAVA_OPTS} -XX:MetaspaceSize=96M -XX:MaxMetaspaceSize=256m"
export JAVA_OPTS

echo "Running Java App with following Options: $JAVA_OPTS"

exec gosu java "$@"
