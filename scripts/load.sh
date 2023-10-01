#!/bin/bash

location=$1
message="Hello from ${location} "

while true
do
  hey -z 20s -c 100 -m POST -H "Content-Type: application/json" -d '{"content":"'"$message"'","owner":"unknown","done":"false"}' http://localhost:8080/api/todo
  json=$(curl http://localhost:8080/api/todo)
  echo $json
  count=0
  for id in $(echo "$json" |jq -r ".[] | .id")
    do
     count=$((++count))
     content=$(echo "$json" | jq '.[] | .content as $data | select(.id | startswith("'$id'") ).content')
     content+=" ${content} world";
     echo '{ "id" : "'$id'", "content": "'$content'"}'
     hey -z 5s -c 500 -m PUT -H "Content-Type: application/json" -d '{ "id" : "'$id'", "content": "'content'"}' http://localhost:8080/api/todo
    done
  sleep 1;
done