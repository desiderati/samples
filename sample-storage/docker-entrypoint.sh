#!/bin/sh
#
# Copyright (c) 2024 - Felipe Desiderati
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

# Exit immediately if a command exits with a non-zero status.
set -e

if ! id -u java >/dev/null 2>&1; then
  USER_ID=${LOCAL_USER_ID:-1000}
  echo "Adding 'java' user..."
  useradd -m -d /home/java -u "${USER_ID}" -s /bin/bash java
  chown -R "${USER_ID}":"${USER_ID}" /home/java
  chown -R "${USER_ID}":"${USER_ID}" /opt
  echo "User 'java' added with success!"

  if [ -z "${TZ}" ]; then
    TZ="America/Sao_Paulo"
  fi
  echo ""
  echo "Configuring Timezone: ${TZ}"

  ln -snf /usr/share/zoneinfo/"$TZ" /etc/localtime && echo "$TZ" > /etc/timezone
  apt-get update && apt-get install -y tzdata
  echo "Updating daylight savings configuration!"

  echo ""
  echo "Locales configured:"
  locale -a
fi

# Garante que não terão quebras de linha na variável JAVA_OPTS.
JAVA_OPTS=$(echo "$JAVA_OPTS" | sed ':a;N;$!ba;s/\n/ /g')

# - A parte `:a;N;$!ba;` é um loop que irá ler todo o texto, incluindo as quebras de linha.
# - A parte `s/\n/ /g` substitui todas as quebras de linha (\n) por espaços.

# O loop `:a;N;$!ba;` no comando `sed` é usado para lidar com múltiplas linhas de entrada.
# Aqui está o que cada parte faz:
#
# - `:a` cria um rótulo chamado 'a'.
# - `N` adiciona a próxima linha de entrada ao padrão de espaço do comando `sed`.
# - `$!ba` se não for a última linha de entrada, vá para o rótulo 'a'.
#
# Este loop é necessário porque, por padrão, o `sed` lê e processa a entrada linha por linha.
# Se você tem várias linhas de entrada e quer fazer uma substituição que abrange várias linhas
# (como substituir quebras de linha por espaços), você precisa primeiro dizer ao `sed` para ler
# todas as linhas de entrada de uma vez. Isso é o que o loop `:a;N;$!ba;` faz.
#
# Se você sabe que sua entrada sempre será uma única linha, você pode omitir o loop e usar apenas `s/\n/ /g`
# para substituir as quebras de linha por espaços. No entanto, se sua entrada pode ter várias linhas,
# é melhor usar o loop para garantir que todas as quebras de linha sejam substituídas corretamente.

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

echo "Running Java App with following Options: ${JAVA_OPTS}"

# shellcheck disable=SC2002
cat /proc/meminfo
java -XX:+PrintFlagsFinal -version | grep ThreadStackSize

exec gosu java "$@"
