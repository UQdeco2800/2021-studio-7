package com.deco2800.game.utils.math;

public class IntUtils {

    public static String[] lettersToIntegers = {
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine",
            "ten", "eleven", "twelve", "thirteen", "fourteen",
            "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
            "twenty", "twenty-one", "twenty-two", "twenty-three", "twenty-four"
    };

    public static int strDigitsToInt(String digits) {
        int num = 0;
        for (int i = 0; i < digits.length(); i++) {
            if (digits.charAt(i) < 48 || digits.charAt(i) > 57) {
                return -1;
            }
            num = num * 10 + (digits.charAt(i) - 48);
        }
        return num;
    }

    public static int strLettersToInt(String letters) {
        for (int i = 0; i < lettersToIntegers.length; i++) {
            if (lettersToIntegers[i].equals(letters)) {
                return i;
            }
        }
        return -1;
    }

    public static String intToStrLetters(int num) {
        if (num < 0 || num > lettersToIntegers.length) {
            return "";
        }
        return lettersToIntegers[num];
    }

    private IntUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
