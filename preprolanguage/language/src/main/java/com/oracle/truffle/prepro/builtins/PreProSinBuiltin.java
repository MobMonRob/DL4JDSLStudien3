package com.oracle.truffle.prepro.builtins;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.runtime.types.*;
import org.nd4j.linalg.ops.transforms.Transforms;

@NodeInfo(shortName = "sin")
public abstract class PreProSinBuiltin extends PreProBuiltinNode {

    @Specialization
    public PreProConstant sin(PreProConstant constant) {
        return new PreProConstant(Transforms.sin(constant.timeSeries(), true));
    }

    @Specialization
    public PreProMatrix3 sin(PreProMatrix3 matrix) {
        return new PreProMatrix3(Transforms.sin(matrix.timeSeries(), true));
    }

    @Specialization
    public PreProMatrix4 sin(PreProMatrix4 matrix) {
        return new PreProMatrix4(Transforms.sin(matrix.timeSeries(), true));
    }

    @Specialization
    public PreProMatrix sin(PreProMatrix matrix) {
        return new PreProMatrix(Transforms.sin(matrix.timeSeries(), true));
    }

    @Specialization
    public PreProScalar sin(PreProScalar scalar) {
        return new PreProScalar(Transforms.sin(scalar.timeSeries(), true));
    }

    @Specialization
    public PreProVector3 sin(PreProVector3 vector3) {
        return new PreProVector3(Transforms.sin(vector3.timeSeries(), true));
    }


    @Specialization
    public PreProVector4 sin(PreProVector4 vector4) {
        return new PreProVector4(Transforms.sin(vector4.timeSeries(), true));
    }

    @Specialization
    public PreProVector sin(PreProVector vector) {
        return new PreProVector3(Transforms.sin(vector.timeSeries(), true));
    }
}
