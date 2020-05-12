package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

@ExportLibrary(InteropLibrary.class)
public class PreProVector3 extends PreProVector implements TruffleObject {

    public PreProVector3(INDArray ndArray) {
        super(ndArray);
        int size = ndArray.shape()[1];
        if (ndArray.shape().length != 2 || size != 3) {
            throw new RuntimeException("The given Vector3 has the size "
                    + size + ", must be 3.");
        }
    }

    @TruffleBoundary
    public PreProVector3 add(PreProVector3 right) {
        return new PreProVector3(timeSeries().add(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProVector3 sub(PreProVector3 right) {
        return new PreProVector3(timeSeries().sub(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProVector3 mul(PreProScalar right) {
        return new PreProVector3(this.timeSeries().mul(right.timeSeries().getScalar(0)));
    }

    @TruffleBoundary
    public PreProVector3 mul(PreProVector3 right) {
        return new PreProVector3(timeSeries().mul(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProVector3 add(PreProConstant right) {
        return new PreProVector3(timeSeries().add(right.timeSeries().getDouble(0)));
    }

    @TruffleBoundary
    public PreProVector3 sub(PreProConstant right) {
        return new PreProVector3(timeSeries().sub(right.timeSeries().getDouble(0)));
    }

    @TruffleBoundary
    public PreProVector3 mul(PreProConstant right) {
        return new PreProVector3(timeSeries().mul(right.timeSeries().getDouble(0)));
    }

    @TruffleBoundary
    public PreProVector3 div(PreProConstant right) {
        return new PreProVector3(timeSeries().div(right.timeSeries().getDouble(0)));
    }

    @TruffleBoundary
    public PreProVector3 crossProduct(PreProVector3 right) {
        if (this.amountTimeElements() != right.amountTimeElements()) {
            throw new RuntimeException("Vectors must have the same amount of time elements.");
        }

        INDArray a = this.timeSeries();
        INDArray b = right.timeSeries();

        INDArray a1 = a.getColumn(0);
        INDArray a2 = a.getColumn(1);
        INDArray a3 = a.getColumn(2);

        INDArray b1 = b.getColumn(0);
        INDArray b2 = b.getColumn(1);
        INDArray b3 = b.getColumn(2);

        INDArray c1 = (a2.mul(b3)).sub(a3.mul(b2));
        INDArray c2 = (a3.mul(b1)).sub(a1.mul(b3));
        INDArray c3 = (a1.mul(b2)).sub(a2.mul(b1));

        int size = a.shape()[0];
        INDArray result = Nd4j.create(size, 3);
        result.putColumn(0, c1);
        result.putColumn(1, c2);
        result.putColumn(2, c3);

        return new PreProVector3(result);
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return timeSeries().shapeInfoToString();
    }
}
