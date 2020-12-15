package com.kardidev.poc.cloud.probes.consumer.service.common;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class Utils {

    public static List<Integer> divide(int length, int count, Random random) {

        if (count < 2 || length < 2)
            return Lists.newArrayList(length);

        int point = random.nextInt(length - 1) + 1;

        if (count == 2)
            return Lists.newArrayList(point, length - point);

        List<Integer> result = Lists.newArrayList();

        int countLeft = count / 2 + count % 2;
        int countRight = count / 2;

        result.addAll(divide(point, countLeft, random));
        result.addAll(divide(length - point, countRight, random));

        return result;
    }

    public static int randomGaussianPositiveInt(int mean, double factor, Random random) {

        int num = (int) (random.nextGaussian() * mean * factor + mean);
        // limiting the range
        return (num <= 0 || num > mean * 2) ? mean : num;
    }

}
