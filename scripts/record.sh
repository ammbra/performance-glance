#!/bin/bash
## This script overwrites the recording.jfr content every 60s and it updates the event.txt content as well.
while true
do
  echo "jcmd 1 JFR.dump name=1"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.dump name=1"
  echo "jcmd 1 JFR.view allocation-by-site"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.view allocation-by-site > recordings/allocation.txt"
  echo "jcmd 1 JFR.view memory-leaks-by-site"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.view memory-leaks-by-site > recordings/mem_leaks.txt"
  echo "jcmd 1 JFR.view hot-methods"
  docker exec -d -it springboot-todo-service sh -c "jcmd 1 JFR.view hot-methods > recordings/hot-methods.txt"
  sleep 60;
done
