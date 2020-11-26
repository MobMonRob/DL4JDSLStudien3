package com.oracle.truffle.prepro.builtins;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.runtime.types.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

@NodeInfo(shortName = "cos")
public abstract class PreProCosBuiltin extends PreProBuiltinNode {

    @Specialization
    public PreProConstant cos(PreProConstant constant) {
        return new PreProConstant(applyCos(constant.timeSeries()));
    }

    @Specialization
    public PreProMatrix3 cos(PreProMatrix3 matrix) {
        return new PreProMatrix3(applyCos(matrix.timeSeries()));
    }

    @Specialization
    public PreProMatrix4 cos(PreProMatrix4 matrix) {
        return new PreProMatrix4(applyCos(matrix.timeSeries()));
    }

    @Specialization
    public PreProMatrix cos(PreProMatrix matrix) {
        return new PreProMatrix(applyCos(matrix.timeSeries()));
    }

    @Specialization
    public PreProScalar cos(PreProScalar scalar) {
        return new PreProScalar(applyCos(scalar.timeSeries()));
    }

    @Specialization
    public PreProVector3 cos(PreProVector3 vector3) {
        return new PreProVector3(applyCos(vector3.timeSeries()));
    }


    @Specialization
    public PreProVector4 cos(PreProVector4 vector4) {
        return new PreProVector4(applyCos(vector4.timeSeries()));
    }

    @Specialization
    public PreProVector cos(PreProVector vector) {
        return new PreProVector3(applyCos(vector.timeSeries()));
    }

    @TruffleBoundary
    private static INDArray applyCos(INDArray original) {
        return Transforms.cos(original, true);
    }

}
