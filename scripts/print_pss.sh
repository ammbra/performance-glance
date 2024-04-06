#!/bin/bash
# This script is from Poonam Parhar's article https://poonamparhar.github.io/troubleshooting_native_memory_leaks/
pid=1
echo 'Monitoring PSS of the Process:' $pid
while true
do
   cat /proc/$pid/smaps | grep -i 'Pss:' |  awk '{Total+=$2} END {print Total}'
  sleep 2
done

