#!/bin/bash
while true
do
  hey -z 30s -c 500 -m POST -H "Content-Type: application/json" -d '{"content":"wow","owner":"unknown","done":"false"}' http://localhost:8080/api/todo
  json=$(curl http://localhost:8080/api/todo)
  echo $json
  for id in $(echo "$json" |jq -r ".[] | .id")
    do
     curl --silent --output /dev/null -X PUT -H "Content-Type: application/json" -d '{ "id" : "'$id'", "content": "now_'$id'"}' http://localhost:8080/api/todo
     curl --silent --output /dev/null -X DELETE -H "Content-Type: application/json" http://localhost:8080/api/todo/$id
    done
  sleep 1;
done
