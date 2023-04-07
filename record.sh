#!/bin/bash
## This script overwrites the recording.jfr content every 60s and it updates the event.txt content as well.
while true
do
  echo "jcmd 1 JFR.dump name=1"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.dump name=1"
  docker exec -d -it springboot-todo-service sh -c "jfr print --events OldObjectSample recordings/recording.jfr>recordings/event.txt"
  sleep 60;
done