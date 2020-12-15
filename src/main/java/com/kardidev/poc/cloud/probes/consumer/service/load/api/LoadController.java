package com.kardidev.poc.cloud.probes.consumer.service.load.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kardidev.poc.cloud.probes.consumer.service.load.LoadService;
import com.kardidev.poc.cloud.probes.consumer.service.monitor.dto.PodRecord;

@RestController
public class LoadController {

    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }

    /**
     * Used to load a certain pod bypassing load balancer.
     * It will help to emulate the situation, when several big requests get to the same pod due to either random or round-robin distribution.
     *
     * @param podIP  target pod IP
     * @param weight a weight it's supposed to be load with in milliseconds
     * @return Http status 200, if the request is accepted by the pod
     */
    @GetMapping("/load/{podIP}")
    public ResponseEntity<PodRecord> load(@PathVariable("podIP") String podIP, @RequestParam("weight") int weight) {
        loadService.load(podIP, weight);
        return ResponseEntity.ok().build();
    }
}
