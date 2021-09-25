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
        for (int y = 0; y < matrix.length / 2; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                T temp = matrix[y][x];
                matrix[y][x] = matrix[matrix.length - y - 1][x];
                matrix[matrix.length - y - 1][x] = temp;
            }
        }
        return matrix;
    }

    public static <T> T[][] flipHorizontally(T[][] matrix) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length / 2; x++) {
                T temp = matrix[y][x];
                matrix[y][x] = matrix[y][matrix[y].length - x - 1];
                matrix[y][matrix[y].length - x - 1] = temp;
            }
        }
        return matrix;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[][] transpose(T[][] matrix) {
        T[][] transposed = (T[][]) Array.newInstance(matrix.getClass(), matrix[0].length, matrix.length);
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                transposed[y][x] = matrix[x][y];
            }
        }
        return transposed;
    }

    private MatrixUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
