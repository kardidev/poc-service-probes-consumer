package com.kardidev.poc.cloud.probes.consumer.service.monitor;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.net.InetAddresses;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.kardidev.poc.cloud.probes.consumer.service.monitor.dto.PodRecord;

/**
 * Requests and caches latest state records from running pods.
 * Cache is needed to allow consuming scripts requesting this information without any delay.
 */
@Service
public class PodStatsCachingService {

    private final StatExtService statExtService;

    private LoadingCache<String, PodRecord> cache;

    public PodStatsCachingService(StatExtService statExtService) {
        this.statExtService = statExtService;
    }

    @PostConstruct
    void init() {
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .build(CacheLoader.asyncReloading(
                        new CacheLoader<String, PodRecord>() {
                            @Override
                            public PodRecord load(String key) {

                                PodRecord record = statExtService.retrievePodStats(key);
                                record.setHealthy(statExtService.retrievePodReadinessState(key));
                                return record;
                            }
                        },
                        Executors.newFixedThreadPool(10)));
    }

    public Optional<PodRecord> readPodStats(String podIP) {

        //noinspection UnstableApiUsage
        if (!InetAddresses.isInetAddress(podIP))
            throw new IllegalArgumentException("IP address is expected");

        try {
            return Optional.of(cache.get(podIP));
        } catch (Exception ex) {

            // if pod is unavailable by IP
            if (ex.getCause() instanceof ResourceAccessException)
                return Optional.empty();

            throw new UncheckedExecutionException(ex);
        }
    }
}
