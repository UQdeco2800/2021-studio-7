package com.deco2800.game.utils.math;

import com.badlogic.gdx.utils.Array;

public class ArrayMatrixUtils {

    public static Array<Array<String>> rotateClockwise(Array<Array<String>> matrix) {
        return reverse(transpose(matrix));
    }

    public static Array<Array<String>> transpose(Array<Array<String>> matrix) {
        Array<Array<String>> transposed = new Array<>(matrix.size);
        for (int i = 0; i < matrix.size; i++) {
            transposed.add(new Array<>(matrix.get(i).size));
            for (int j = 0; j < matrix.get(i).size; j++) {
                transposed.get(i).add(matrix.get(i).get(j));
            }
        }
        return transposed;
    }

    public static Array<Array<String>> reverse(Array<Array<String>> matrix) {
        for (Array<String> column : matrix) {
            column.reverse();
        }
        return matrix;
    }

    private ArrayMatrixUtils() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
