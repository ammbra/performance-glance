#!/bin/bash

echo "jstat -gcutil -h10  1 500 50"
docker exec -it springboot-todo-service sh -c "jstat -gcutil -h10 1 500 50 > /recordings/gcstats.txt"

read -p "Print heap summaries? " -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
  echo "jmap -histo 1"
  docker exec -it springboot-todo-service sh -c "jmap -histo 1 > /recordings/histoe.txt"
fi