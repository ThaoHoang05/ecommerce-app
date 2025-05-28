package com.stationeryshop.utils;

import java.util.Random;

public class RandomUserId {
    private static Random random = new Random();
    public String getRandomUserId(){
        return String.valueOf(random.nextInt(99999));
    }
}
