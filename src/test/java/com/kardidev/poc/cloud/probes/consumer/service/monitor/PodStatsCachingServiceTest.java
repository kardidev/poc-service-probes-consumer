package com.kardidev.poc.cloud.probes.consumer.service.monitor;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kardidev.poc.cloud.probes.consumer.service.monitor.dto.PodRecord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PodStatsCachingServiceTest {

    @InjectMocks
    private PodStatsCachingService service;
    @Mock
    private StatExtService statExtService;

    @Before
    public void setUp() {

        service.init();

        when(statExtService.retrievePodStats(anyString()))
                .thenAnswer(args -> {
                    Thread.sleep(1000); // simulating latency
                    return PodRecord.builder()
                            .ip(args.getArgument(0))
                            .build();
                });
    }

    @Test
    public void testReadPodStats_whenKeyIsRequestedForTheFirstTime_shouldLoadValueSynchronously() {
        // Having
        String ip = "1.1.1.1";

        // When
        Optional<PodRecord> value = service.readPodStats(ip);

        // Then
        verify(statExtService).retrievePodStats(anyString());
        assertThat(value.isPresent()).isTrue();
        assertThat(value.get().getIp()).isEqualTo(ip);
    }

    @Test
    public void testReadPodStats_whenKeyIsRequestedMultipleTimesInLessThanSecond_shouldLoadValueSynchronouslyOnlyOnce() {
        // Having
        String ip = "1.1.1.1";

        // When
        Optional<PodRecord> value1 = service.readPodStats(ip);
        Optional<PodRecord> value2 = service.readPodStats(ip);

        // Then
        assertThat(value1.isPresent()).isTrue();
        assertThat(value1.get().getIp()).isEqualTo(ip);
        assertThat(value2.isPresent()).isTrue();
        assertThat(value2.get().getIp()).isEqualTo(ip);
        verify(statExtService).retrievePodStats(anyString());
    }

    @Test
    public void testReadPodStats_whenKeyIsRequestedTwoTimesWithIntervalWhichIsGreaterThanExpirationOne_shouldLoadValueTwoTimesWhereOnlyFirstIsSynchronous() throws InterruptedException {
        // Having
        String ip = "1.1.1.1";

        // When
        Optional<PodRecord> value1 = service.readPodStats(ip);
        Thread.sleep(2000); // default expiration time is 1 sec
        Optional<PodRecord> value2 = service.readPodStats(ip);

        // Then
        assertThat(value1.isPresent()).isTrue();
        assertThat(value1.get().getIp()).isEqualTo(ip);
        assertThat(value2.isPresent()).isTrue();
        assertThat(value2.get().getIp()).isEqualTo(ip);
        verify(statExtService, times(2)).retrievePodStats(anyString());
    }
}