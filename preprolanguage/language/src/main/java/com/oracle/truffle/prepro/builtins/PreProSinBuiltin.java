package com.oracle.truffle.prepro.builtins;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.runtime.types.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

@NodeInfo(shortName = "sin")
public abstract class PreProSinBuiltin extends PreProBuiltinNode {

    @Specialization
    public PreProConstant sin(PreProConstant constant) {
        return new PreProConstant(applySin(constant.timeSeries()));
    }

    @Specialization
    public PreProMatrix3 sin(PreProMatrix3 matrix) {
        return new PreProMatrix3(applySin(matrix.timeSeries()));
    }

    @Specialization
    public PreProMatrix4 sin(PreProMatrix4 matrix) {
        return new PreProMatrix4(applySin(matrix.timeSeries()));
    }

    @Specialization
    public PreProMatrix sin(PreProMatrix matrix) {
        return new PreProMatrix(applySin(matrix.timeSeries()));
    }

    @Specialization
    public PreProScalar sin(PreProScalar scalar) {
        return new PreProScalar(applySin(scalar.timeSeries()));
    }

    @Specialization
    public PreProVector3 sin(PreProVector3 vector3) {
        return new PreProVector3(applySin(vector3.timeSeries()));
    }
    

    @Specialization
    public PreProVector4 sin(PreProVector4 vector4) {
        return new PreProVector4(applySin(vector4.timeSeries()));
    }
        
    @Specialization
    public PreProVector sin(PreProVector vector) {
        return new PreProVector3(applySin(vector.timeSeries()));
    }
    
    @TruffleBoundary
    private static INDArray applySin(INDArray original){
        return Transforms.sin(original, true);
    }
}
