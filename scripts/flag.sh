#!/bin/bash

echo
echo "jcmd 1 VM.flags"
docker exec -it springboot-todo-service sh -c "jcmd 1 VM.flags"

echo
read -p "Run jcmd to see native memory? (y/n) " -n 1 -r
echo    # (optional) move to a new line
echo
if [[ $REPLY =~ ^[Yy]$ ]]
then
  echo "jcmd 1 VM.native_memory"
  docker exec -it springboot-todo-service sh -c "jcmd 1 VM.native_memory"
fi

echo
read -p "Run jinfo to check NativeMemoryTracking flag? " -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
  echo "jinfo -flag NativeMemoryTracking 1"
  docker exec -it springboot-todo-service sh -c "jinfo -flag NativeMemoryTracking 1"
fi