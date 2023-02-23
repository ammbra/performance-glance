#!/bin/bash
while true
do
  hey -z 10s -c 10 -m POST -H "Content-Type: application/json" -d '{"content":"now","owner":"unknown","finish":"false"}' http://localhost:8080/api/todo
  hey -z 10s -c 10 -m POST -H "Content-Type: application/json" -d '{"content":"now","owner":"unknown","finish":"false"}' http://localhost:8082/api/todo
  sleep 90;
  hey -z 10s -c 10 -m PUT -H "Content-Type: application/json" -d '{ id: "1ff78fc9-3b45-43d6-ae34-8459b9eb91ad", "content": "now", "owner": "unknown" }' http://localhost:8080/api/todo
  hey -z 10s -c 10 -m PUT -H "Content-Type: application/json" -d '{ id: "1ff78fc9-3b45-43d6-ae34-8459b9eb91ad", "content": "now", "owner": "unknown" }' http://localhost:8082/api/todo
  sleep 90;
  hey -z 10s -c 10 -m GET -H "Content-Type: application/json"  http://localhost:8080/api/todo
  hey -z 10s -c 10 -m GET -H "Content-Type: application/json"  http://localhost:8082/api/todo
  sleep 90;

done