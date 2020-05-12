package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class PreProMatrix implements TruffleObject {

    private final INDArray ndArray;

    public PreProMatrix(INDArray ndArray) {
        this.ndArray = ndArray;
        if (ndArray.shape().length != 3) {
            throw new RuntimeException("Matrix must be two-dimensional.");
        }
    }

    public INDArray timeSeries() {
        return ndArray;
    }

    public int amountTimeElements() {
        return ndArray.shape()[0];
    }

    INDArray multiplyMatrixWithMatrix(PreProMatrix left, PreProMatrix right, int dimension) {
        INDArray result = Nd4j.create(left.amountTimeElements(), dimension, dimension);
        for (int i = 0; i < left.amountTimeElements(); i++) {
            result.putRow(i, left.timeSeries().getRow(i).mmul(right.timeSeries().getRow(i)));
        }
        return result;
    }

    INDArray multiplyMatrixWithVector(PreProMatrix left, PreProVector right, int dimension) {
        INDArray result = Nd4j.create(left.amountTimeElements(), dimension);
        for (int i = 0; i < left.amountTimeElements(); i++) {
            INDArray leftValue = left.timeSeries().getRow(i);
            INDArray rightValue = right.timeSeries().getRow(i);

            INDArray value = leftValue.mmul(rightValue.transpose());
            result.putRow(i, value);
        }

        return result;
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return timeSeries().shapeInfoToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreProMatrix)) return false;
        PreProMatrix that = (PreProMatrix) o;
        return ndArray.equals(that.ndArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ndArray);
    }
}
