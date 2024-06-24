package com.example.flowerapp.util;

import java.security.SecureRandom;

public class UniqueKeyGenerator {

    public static String generateUniqueKey(){
        long timestamp = System.currentTimeMillis();

        SecureRandom secureRandom = new SecureRandom();
        int randomNumber = secureRandom.nextInt(100000);

        return "ZA-"+ timestamp + "-" + randomNumber;
    }
}
