#!/bin/bash
set -e


if [ "$ENV" = 'docker' ]; then
  echo "Running from Docker"
  exec java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$ENV -jar app.jar;
elif [ "$ENV" = 'dev' ]; then
  echo "Running from dev"
  exec java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=$ENV -jar  app.jar;
else
  echo "Running on Production"
  exec java -Djava.security.egd=file:/dev/./urandom -Dcom.sun.management.jmxremote.ssl=false \
                                                    -Dcom.sun.management.jmxremote.authenticate=false \
                                                    -Djava.rmi.server.hostname='localhost' \
                                                    -Dcom.sun.management.jmxremote.rmi.port=1099 \
                                                    -Dcom.sun.management.jmxremote.port=1099 \
                                                    -Dcom.sun.management.jmxremote=true \
                                                    -jar app.jar
fi
