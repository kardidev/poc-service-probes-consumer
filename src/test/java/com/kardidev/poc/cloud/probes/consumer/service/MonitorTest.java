package com.kardidev.poc.cloud.probes.consumer.service;

import org.junit.Test;

public class MonitorTest {


    @Test
    public void testStats() {

//        ProcessBuilder builder = new ProcessBuilder();
//        builder.command("sh", "-c", "ls");
//        builder.directory(new File(System.getProperty("user.home")));
//        Process process = builder.start();
//        StreamGobbler streamGobbler =
//                new StreamGobbler(process.getInputStream(), System.out::println);
//        Executors.newSingleThreadExecutor().submit(streamGobbler);
//        int exitCode = process.waitFor();
//        assert exitCode == 0;

    }


    private String getPodsNamesCommand() {
        return "kubectl get pods --no-headers -o custom-columns=:metadata.name";
    }
}
