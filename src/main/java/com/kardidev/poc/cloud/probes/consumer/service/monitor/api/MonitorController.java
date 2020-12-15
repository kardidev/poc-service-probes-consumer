package com.kardidev.poc.cloud.probes.consumer.service.monitor.api;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kardidev.poc.cloud.probes.consumer.service.monitor.PodStatsCachingService;
import com.kardidev.poc.cloud.probes.consumer.service.monitor.dto.PodRecord;

@RestController
public class MonitorController {

    private final PodStatsCachingService podStatsCachingService;

    public MonitorController(PodStatsCachingService podStatsCachingService) {
        this.podStatsCachingService = podStatsCachingService;
    }

    /**
     * Reads a state of a target pod.
     *
     * @param podIP target pod IP
     * @return PodRecord structure
     */
    @GetMapping("/state/{podIP}")
    public ResponseEntity<PodRecord> state(@PathVariable("podIP") String podIP) {

        Optional<PodRecord> optionalPodRecord = podStatsCachingService.readPodStats(podIP);

        return optionalPodRecord
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
