#!/bin/bash
while true
do
  hey -z 3s -c 3 -m POST -H "Content-Type: application/json" -d '{"content":"wow","owner":"unknown","done":"false"}' http://localhost:8080/api/todo
  json=$(curl http://localhost:8080/api/todo)
  echo $json
  for i in $(echo "$json" |jq -r ".[] | .id")
    do
     curl -X PUT -H "Content-Type: application/json" -d '{ "id" : "'$i'", "content": "now_'$i'"}' http://localhost:8080/api/todo
    done
  sleep 10;
  for i in $(echo "$json" |jq -r ".[] | .id")
      do
        curl -X DELETE -H "Content-Type: application/json" http://localhost:8080/api/todo/$i
      done
  sleep 10;
done
