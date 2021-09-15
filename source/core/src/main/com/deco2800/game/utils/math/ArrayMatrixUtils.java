package com.deco2800.game.utils.math;

import com.badlogic.gdx.utils.Array;

public class ArrayMatrixUtils {

    public static String[][] rotateClockwise(String[][] matrix) {
        return reverse((matrix));
    }

    public static String[][] transpose(String[][] matrix) {
        String[][] transposed = new String[matrix.length][matrix.length];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length; y++) {
                transposed[x][y] = matrix[y][x];
            }
        }
        return transposed;
    }

    public static String[][] reverse(String[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length / 2; y++) {
                String temp = matrix[x][y];
                matrix[x][y] = matrix[x][matrix.length - y - 1];
                matrix[x][matrix.length - y - 1] = temp;
            }
        }
        return matrix;
    }

    private ArrayMatrixUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
