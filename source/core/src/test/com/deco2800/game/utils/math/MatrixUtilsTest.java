package com.deco2800.game.utils.math;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

@ExtendWith(GameExtension.class)
class MatrixUtilsTest {

    @Test
    void shouldSimpleFlipHorizontally() {
        assert Arrays.deepEquals(MatrixUtils.flipHorizontally(SimpleMatrix.base),
                SimpleMatrix.flippedHorizontally);
    }

    @Test
    void shouldSimpleFlipVertically() {
        assert Arrays.deepEquals(MatrixUtils.flipVertically(SimpleMatrix.base),
                SimpleMatrix.flippedVertically);
    }

    @Test
    void shouldSimpleTranspose() {
        assert Arrays.deepEquals(MatrixUtils.transpose(SimpleMatrix.base),
                SimpleMatrix.transposed);
    }

    @Test
    void shouldSimpleRotateClockwise() {
        assert Arrays.deepEquals(MatrixUtils.rotateClockwise(SimpleMatrix.base),
                SimpleMatrix.rotatedClockwise);
    }

    @Test
    void shouldSimpleRotateAntiClockwise() {
        assert Arrays.deepEquals(MatrixUtils.rotateAntiClockwise(SimpleMatrix.base),
                SimpleMatrix.rotatedAntiClockwise);
    }

    @Test
    void shouldNonSquareFlipHorizontally() {
        assert Arrays.deepEquals(MatrixUtils.flipHorizontally(NonSquareMatrix.base),
                NonSquareMatrix.flippedHorizontally);
    }

    @Test
    void shouldNonSquareFlipVertically() {
        assert Arrays.deepEquals(MatrixUtils.flipVertically(NonSquareMatrix.base),
                NonSquareMatrix.flippedVertically);
    }

    @Test
    void shouldNonSquareTranspose() {
        assert Arrays.deepEquals(MatrixUtils.transpose(NonSquareMatrix.base),
                NonSquareMatrix.transposed);
    }

    @Test
    void shouldNonSquareRotateClockwise() {
        assert Arrays.deepEquals(MatrixUtils.rotateClockwise(NonSquareMatrix.base),
                NonSquareMatrix.rotatedClockwise);
    }

    @Test
    void shouldNonSquareRotateAntiClockwise() {
        assert Arrays.deepEquals(MatrixUtils.rotateAntiClockwise(NonSquareMatrix.base),
                NonSquareMatrix.rotatedAntiClockwise);
    }
    
    static class SimpleMatrix {
        static Character[][] base = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'}
        };
        static Character[][] flippedHorizontally = {
                {'c', 'b', 'a'},
                {'f', 'e', 'd'},
                {'i', 'h', 'g'}
        };
        static Character[][] flippedVertically = {
                {'g', 'h', 'i'},
                {'d', 'e', 'f'},
                {'a', 'b', 'c'}
        };
        static Character[][] transposed = {
                {'a', 'd', 'g'},
                {'b', 'e', 'h'},
                {'c', 'f', 'i'}
        };
        static Character[][] rotatedClockwise = {
                {'g', 'd', 'a'},
                {'h', 'e', 'b'},
                {'i', 'f', 'c'}
        };
        static Character[][] rotatedAntiClockwise = {
                {'c', 'f', 'i'},
                {'b', 'e', 'h'},
                {'a', 'd', 'g'}
        };
    }
    
    static class NonSquareMatrix {
        static Character[][] base = {
                {'a', 'b', 'c'},
                {'d', 'e', 'f'}
        };
        static Character[][] flippedHorizontally = {
                {'c', 'b', 'a'},
                {'f', 'e', 'd'}
        };
        static Character[][] flippedVertically = {
                {'d', 'e', 'f'},
                {'a', 'b', 'c'}
        };
        static Character[][] transposed = {
                {'a', 'd'},
                {'b', 'e'},
                {'c', 'f'}
        };
        static Character[][] rotatedClockwise = {
                {'d', 'a'},
                {'e', 'b'},
                {'f', 'c'}
        };
        static Character[][] rotatedAntiClockwise = {
                {'c', 'f'},
                {'b', 'e'},
                {'a', 'd'}
        };
    }
}
