package com.kardidev.poc.cloud.probes.consumer.service.common;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

import com.google.common.collect.Lists;


public class UtilsTest {

    @Test
    public void testDivide() {
        // Given
        int length = 50;
        int maxSegments = 5;
        Random random = new Random(42);

        // When
        List<Integer> segments = Utils.divide(length, maxSegments, random);

        // Then
        int sum = segments.stream().mapToInt(Integer::intValue).sum();
        assert sum == length;
        assert segments.equals(Lists.newArrayList(1, 9, 20, 7, 13));

        System.out.println("segments = " + segments.size());
        System.out.println("sum = " + sum);
        segments.forEach(System.out::println);
    }

    @Test
    public void testDivide_whenCountEqualsOne() {
        // Given
        int length = 50;
        int maxSegments = 1;
        Random random = new Random(42);

        // When
        List<Integer> segments = Utils.divide(length, maxSegments, random);

        // Then
        int sum = segments.stream().mapToInt(Integer::intValue).sum();
        assert sum == length;
        assert segments.size() == 1;

        System.out.println("segments = " + segments.size());
        System.out.println("sum = " + sum);
        segments.forEach(System.out::println);
    }

    @Test
    public void testNextGaussian() {
        Random random = new Random(42);

        int[] distribution = new int[11];
        IntStream.range(1, 1000)
                .forEach(x -> {
                    int gauss = Utils.randomGaussianPositiveInt(50, 0.7, random);
                    int index = gauss / 10;
                    distribution[index]++;
                });

        for (int i = 0; i < distribution.length; i++) {
            System.out.print(distribution[i] + ",");
        }
        System.out.println();

    }

    @Test
    public void testNextGaussian_average() {
        Random random = new Random(42);
        int averageWeight = 5;

        IntStream.range(0, 40).forEach(x -> {
            System.out.print(Utils.randomGaussianPositiveInt(averageWeight, 0.5, random) + ",");
        });
    }

}