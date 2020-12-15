package com.kardidev.poc.cloud.probes.consumer.service.monitor;

import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.kardidev.poc.cloud.probes.consumer.service.monitor.dto.PodRecord;

@Service
public class StatExtService {

    private final RestTemplate restTemplate;

    public StatExtService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves pod statistics by IP.
     *
     * @param podIP target pod IP address
     * @return PodRecord with runtime statistic
     */
    public PodRecord retrievePodStats(String podIP) {

        String url = String.format("http://%s:8080/frontservice/stats", podIP);

        ResponseEntity<RequestPoolStats> responseEntity =
                restTemplate.getForEntity(url, RequestPoolStats.class);

        if (responseEntity.getStatusCode() != HttpStatus.OK
                || responseEntity.getBody() == null)
            throw new RuntimeException("Unexpected response from frontend service");

        RequestPoolStats result = responseEntity.getBody();
        return PodRecord.builder()
                .ip(podIP)
                .activeTasks(result.getActiveTasks())
                .queuedTasks(result.getQueuedTasks())
                .completedTasks(result.getCompletedTasks())
                .rejectedTasks(result.getRejectedTasks())
                .build();
    }

    /**
     * Retrieves pod readiness state.
     *
     * @param podIP target pod IP address
     * @return true, if pod is healthy and ready to process requests.
     */
    public boolean retrievePodReadinessState(String podIP) {

        try {
            String url = String.format("http://%s:8080/frontservice/manage/health/readiness", podIP);
            ResponseEntity<String> responseEntity =
                    restTemplate.getForEntity(url, String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;

        } catch (HttpStatusCodeException ex) {
            return false;
        }
    }

    @Data
    private static class RequestPoolStats {
        private int activeTasks;
        private int queuedTasks;
        private long completedTasks;
        private long rejectedTasks;
    }
}
