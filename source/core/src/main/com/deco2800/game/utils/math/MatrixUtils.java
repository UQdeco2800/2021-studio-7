package com.deco2800.game.utils.math;

import java.lang.reflect.Array;

@SuppressWarnings("unchecked")
public class MatrixUtils {

    public static <T> T[][] rotateClockwise(T[][] matrix) {
        return transpose(flipVertically(matrix));
    }

    public static <T> T[][] rotateAntiClockwise(T[][] matrix) {
        return flipVertically(transpose(matrix));
    }

    public static <T> T[][] flipVertically(T[][] matrix) {
        T[][] flipped = (T[][]) Array.newInstance(matrix[0][0].getClass(), matrix.length, matrix[0].length);
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                flipped[y][x] = matrix[matrix.length - y - 1][x];
                flipped[matrix.length - y - 1][x] = matrix[y][x];
            }
        }
        return flipped;
    }

    public static <T> T[][] flipHorizontally(T[][] matrix) {
        T[][] flipped = (T[][]) Array.newInstance(matrix[0][0].getClass(), matrix.length, matrix[0].length);
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                flipped[y][x] = matrix[y][matrix[y].length - x - 1];
                flipped[y][matrix[y].length - x - 1] = matrix[y][x];
            }
        }
        return flipped;
    }

    public static <T> T[][] transpose(T[][] matrix) {
        T[][] transposed = (T[][]) Array.newInstance(matrix[0][0].getClass(), matrix[0].length, matrix.length);
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
