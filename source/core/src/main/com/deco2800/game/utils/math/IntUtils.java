package com.deco2800.game.utils.math;

public class IntUtils {

    public static int strToInt(String digits) {
        int num = 0;
        for (int i = 0; i < digits.length(); i++) {
            if (digits.charAt(i) < 48 || digits.charAt(i) > 57) {
                return -1;
            }
            num = num * 10 + (digits.charAt(i) - 48);
        }
        return num;
    }

    private IntUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
