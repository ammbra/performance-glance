#!/bin/bash
while true
do
  hey -z 20s -c 500 -m POST -H "Content-Type: application/json" -d '{"content":"wow","owner":"unknown","done":"false"}' http://localhost:8080/api/todo
  json=$(curl http://localhost:8080/api/todo)
  echo $json
  content="A new content"
  for id in $(echo "$json" |jq -r ".[] | .id")
    do
     content+=" world";
     echo '{ "id" : "'$id'", "content": "'$content'"}'
     curl --silent --output /dev/null -X PUT http://localhost:8080/api/todo -H "Content-Type: application/json" -d '{ "id" : "'"$id"'", "content": "'"$content"'"}'
    done
  sleep 1;
done
