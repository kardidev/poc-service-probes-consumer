#!/bin/bash

#./loader.sh 80 5500 1

repeat="${1}"
weight="${2}"
interval="${3}"

accepted=0
rejected=0

for run in `seq $repeat`
do
  url="http://localhost:8081/frontservice/process?weight=${weight}"
  json_res=$(curl -s ${url})
  podName=$(echo ${json_res} | jq -r '.podName')
  status=$(echo ${json_res} | jq -r '.status')

  if [ "$status" = "ACCEPTED" ]
  then
    accepted=$((accepted+1))
  else
    rejected=$((rejected+1))
  fi

  printf "${run} : ${podName} : ${status}\n"

  sleep $interval
done

printf "Total: ${repeat}   Accepted: ${accepted}   Rejected: ${rejected}\n"
