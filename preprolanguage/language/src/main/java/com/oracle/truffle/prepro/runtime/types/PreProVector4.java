package com.oracle.truffle.prepro.runtime.types;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import org.nd4j.linalg.api.ndarray.INDArray;

@ExportLibrary(InteropLibrary.class)
public class PreProVector4 extends PreProVector implements TruffleObject {
    public PreProVector4(INDArray ndArray) {
        super(ndArray);
        int size = ndArray.shape()[1];
        if (ndArray.shape().length != 2 || size != 4) {
            throw new RuntimeException("The given Vector4 has the size "
                    + size + ", must be 3.");
        }
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return timeSeries().shapeInfoToString();
    }
}
