package com.planner.travel.global.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomNumberUtil {
    public Long set() {
        Random random = new Random();
        long randomNumber = random.nextLong(9000) + 1000;
        return randomNumber;
    }
}
