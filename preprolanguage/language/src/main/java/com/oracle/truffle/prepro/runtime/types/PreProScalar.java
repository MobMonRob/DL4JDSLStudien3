package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Objects;

@ExportLibrary(InteropLibrary.class)
public class PreProScalar implements TruffleObject {

    private final INDArray ndArray;

    public PreProScalar(INDArray ndArray) {
        this.ndArray = ndArray;
        int size = ndArray.shape()[1];
        if (ndArray.shape().length != 2 || size != 1) {
            throw new RuntimeException("The given Scalar has the size "
                    + size + ", must be 1.");
        }
    }

    public INDArray timeSeries() {
        return ndArray;
    }

    public int amountTimeElements() {
        return ndArray.shape()[0];
    }

    @TruffleBoundary
    public PreProScalar add(PreProScalar right) {
        return new PreProScalar(timeSeries().add(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProScalar sub(PreProScalar right) {
        return new PreProScalar(timeSeries().sub(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProScalar mul(PreProScalar right) {
        return new PreProScalar(timeSeries().mul(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProScalar div(PreProScalar right) {
        return new PreProScalar(timeSeries().div(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProScalar isEqualTo(PreProScalar right) {
        double[] result = new double[amountTimeElements()];
        for (int i = 0; i < amountTimeElements(); i++) {
            if (timeSeries().getDouble(i) == right.timeSeries().getDouble(i)) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }

        }
        return new PreProScalar(Nd4j.create(result, new int[]{amountTimeElements(), 1}));
    }

    @TruffleBoundary
    public PreProScalar isLessThan(PreProScalar right) {
        double[] result = new double[amountTimeElements()];
        for (int i = 0; i < amountTimeElements(); i++) {
            if (timeSeries().getDouble(i) < right.timeSeries().getDouble(i)) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }

        }

        return new PreProScalar(Nd4j.create(result, new int[]{amountTimeElements(), 1}));
    }

    @TruffleBoundary
    public PreProScalar isLessOrEqualThan(PreProScalar right) {
        double[] result = new double[amountTimeElements()];
        for (int i = 0; i < amountTimeElements(); i++) {
            if (timeSeries().getDouble(i) <= right.timeSeries().getDouble(i)) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }

        }

        return new PreProScalar(Nd4j.create(result, new int[]{amountTimeElements(), 1}));
    }

    @TruffleBoundary
    public PreProScalar isGreaterThan(PreProScalar right) {
        double[] result = new double[amountTimeElements()];
        for (int i = 0; i < amountTimeElements(); i++) {
            if (timeSeries().getDouble(i) > right.timeSeries().getDouble(i)) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }

        }

        return new PreProScalar(Nd4j.create(result, new int[]{amountTimeElements(), 1}));
    }

    @TruffleBoundary
    public PreProScalar isGreaterOrEqualThan(PreProScalar right) {
        double[] result = new double[amountTimeElements()];
        for (int i = 0; i < amountTimeElements(); i++) {
            if (timeSeries().getDouble(i) >= right.timeSeries().getDouble(i)) {
                result[i] = 1;
            } else {
                result[i] = 0;
            }

        }

        return new PreProScalar(Nd4j.create(result, new int[]{amountTimeElements(), 1}));
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return timeSeries().shapeInfoToString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreProScalar)) return false;
        PreProScalar that = (PreProScalar) o;
        return ndArray.equals(that.ndArray);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ndArray);
    }
}
