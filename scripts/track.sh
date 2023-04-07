#!/bin/bash

echo "jcmd 1 VM.native_memory baseline"
docker exec -it -d springboot-todo-service sh -c "jcmd 1 VM.native_memory baseline"

echo "Wait 10 seconds"
sleep 10

read -p "Run a diff? " -n 1 -r
echo    # (optional) move to a new line
if [[  $REPLY =~ ^[Yy]$ ]]
then
  echo "jcmd 1 VM.native_memory summary.diff"
  docker exec -it -d springboot-todo-service sh -c "jcmd 1 VM.native_memory summary.diff > /recordings/summary.diff.txt"
fi