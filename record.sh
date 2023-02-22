#!/bin/bash
while true
do
  echo "jcmd 1 JFR.dump name=1"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.dump name=1"
  sleep 90;
  curl -F "file=@recordings/recording.jfr" http://localhost:8081/load
  curl http://localhost:8081/list
done

