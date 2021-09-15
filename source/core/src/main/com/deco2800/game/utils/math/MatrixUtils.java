package com.deco2800.game.utils.math;

public class MatrixUtils {

    public static String[][] rotateClockwise(String[][] matrix) {
        return flipVertically(transpose(matrix));
    }

    public static String[][] rotateAntiClockwise(String[][] matrix) {
        return transpose(flipVertically(matrix));
    }

    public static String[][] flipVertically(String[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length / 2; y++) {
                String temp = matrix[x][y];
                matrix[x][y] = matrix[x][matrix.length - y - 1];
                matrix[x][matrix.length - y - 1] = temp;
            }
        }
        return matrix;
    }

    public static String[][] flipHorizontally(String[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix.length / 2; y++) {
                String temp = matrix[x][y];
                matrix[x][y] = matrix[matrix.length - x - 1][y];
                matrix[matrix.length - x - 1][y] = temp;
            }
        }
        return matrix;
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

    private MatrixUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
