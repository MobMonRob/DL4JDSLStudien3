package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

@ExportLibrary(InteropLibrary.class)
public class PreProMatrix4 extends PreProMatrix implements TruffleObject {

    public PreProMatrix4(INDArray ndArray) {
        super(ndArray);
        if (ndArray.shape()[0] != 4 || ndArray.shape()[1] != 4) {
            throw new RuntimeException("The given Matrix4 has the shape "
                    + Arrays.toString(ndArray.shape()) + ", must be [4,4].");
        }
    }

    @TruffleBoundary
    public PreProMatrix4 add(PreProMatrix4 right) {
        return new PreProMatrix4(timeSeries().add(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProMatrix4 sub(PreProMatrix4 right) {
        return new PreProMatrix4(timeSeries().sub(right.timeSeries()));
    }

    @TruffleBoundary
    public PreProMatrix4 mul(PreProMatrix4 right) {
        return new PreProMatrix4(multiplyMatrixWithMatrix(this, right, 4));
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return timeSeries().shapeInfoToString();
    }
}
