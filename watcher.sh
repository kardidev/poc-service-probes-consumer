#!/bin/bash

# watch -t -n 1 ./watcher.sh

clear

printf '\n%-30s %-16s %-15s %-10s %-10s %-10s %-10s %-10s\n' "Name" "IP" "State" "Readiness" "Active" "Queued" "Completed" "Rejected"

kubectl get pod -o wide | grep frontservice | while read rec
do
	read -r -a values <<< "$rec"

	name="${values[0]}"
	state="${values[2]}"
	ip="${values[5]}"
	readiness="${values[8]}"

	url="http://localhost:8082/consumer/state/${ip}"
	
	json_res=$(curl -s ${url})

	active_tasks=$(echo ${json_res} | jq '.activeTasks')
	queued_tasks=$(echo ${json_res} | jq '.queuedTasks')
	completed_tasks=$(echo ${json_res} | jq '.completedTasks')
	rejected_tasks=$(echo ${json_res} | jq '.rejectedTasks')
	healthy=$(echo ${json_res} | jq '.healthy')

	printf '%-30s %-16s %-15s %-10s %-10s %-10s %-10s %-10s\n' "${name}" "${ip}" "${state}" "${healthy}" "${active_tasks}" "${queued_tasks}" "${completed_tasks}" "${rejected_tasks}"
done
