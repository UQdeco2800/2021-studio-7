package com.deco2800.game.utils.math;

import java.lang.reflect.Array;

public class MatrixUtils {

    public static <T> T[][] rotateClockwise(T[][] matrix) {
        return flipVertically(transpose(matrix));
    }

    public static <T> T[][] rotateAntiClockwise(T[][] matrix) {
        return transpose(flipVertically(matrix));
    }

    public static <T> T[][] flipVertically(T[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length / 2; y++) {
                T temp = matrix[x][y];
                matrix[x][y] = matrix[x][matrix.length - y - 1];
                matrix[x][matrix.length - y - 1] = temp;
            }
        }
        return matrix;
    }

    public static <T> T[][] flipHorizontally(T[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length / 2; y++) {
                T temp = matrix[x][y];
                matrix[x][y] = matrix[matrix.length - x - 1][y];
                matrix[matrix.length - x - 1][y] = temp;
            }
        }
        return matrix;
    }

    public static <T> T[][] transpose(T[][] matrix) {
        T[][] transposed = (T[][]) Array.newInstance(matrix.getClass(), matrix[0].length, matrix.length);
        for (int x = 0; x < matrix[0].length; x++) {
            for (int y = 0; y < matrix.length; y++) {
                transposed[x][y] = matrix[y][x];
            }
        }
        return transposed;
    }

    private MatrixUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
