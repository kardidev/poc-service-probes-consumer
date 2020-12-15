package com.kardidev.poc.cloud.probes.consumer.service.monitor.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PodRecord {

    private String ip;

    private Long completedTasks;
    private Long rejectedTasks;
    private Integer activeTasks;
    private Integer queuedTasks;

    @Setter
    private Boolean healthy;
}
