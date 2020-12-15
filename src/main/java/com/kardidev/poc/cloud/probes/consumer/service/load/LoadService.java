package com.kardidev.poc.cloud.probes.consumer.service.load;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoadService {

    private final RestTemplate restTemplate;

    public LoadService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Call a frontservice REST point to add a task with a given weight
     *
     * @param podIP  target pod IP
     * @param weight a weight it's supposed to be load with in milliseconds
     */
    public void load(String podIP, int weight) {

        String url = String.format("http://%s:8080/frontservice/process?weight=%d", podIP, weight);

        ResponseEntity<Object> responseEntity = restTemplate.getForEntity(url, Object.class);

        if (responseEntity.getStatusCode() != HttpStatus.ACCEPTED)
            throw new RuntimeException("Unexpected response from frontend service");
    }

}
