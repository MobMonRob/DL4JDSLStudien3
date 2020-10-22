package com.oracle.truffle.prepro.builtins;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.runtime.types.PreProConstant;

@NodeInfo(shortName = "sin")
public abstract class PreProSinBuiltin extends PreProBuiltinNode {

    @Specialization
    public PreProConstant sin(PreProConstant constant) {
        final double sin = Math.sin(constant.getDoubleValue());
        return new PreProConstant(sin);
    }


}
