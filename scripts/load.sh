#!/bin/bash
while true
do
  hey -z 20s -c 50 -m POST -H "Content-Type: application/json" -d '{"content":"wow","owner":"unknown","done":"false"}' http://localhost:8080/api/todo
  json=$(curl http://localhost:8080/api/todo)
  echo $json
  content="Hello from Devoxx"
  for id in $(echo "$json" |jq -r ".[] | .id")
    do
     content+=" world";
     echo '{ "id" : "'$id'", "content": "'$content'"}'
     hey -z 1s -c 10 -m PUT -H "Content-Type: application/json" -d '{ "id" : "'"$id"'", "content": "'"$content"'"}' http://localhost:8080/api/todo
    done
  sleep 1;
done
