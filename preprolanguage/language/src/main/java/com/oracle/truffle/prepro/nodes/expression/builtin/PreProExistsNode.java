package com.oracle.truffle.prepro.nodes.expression.builtin;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.CachedContext;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.PreProLanguage;
import com.oracle.truffle.prepro.builtins.PreProBuiltinNode;
import com.oracle.truffle.prepro.runtime.PreProContext;

@NodeInfo(shortName = "exists")
public abstract class PreProExistsNode extends PreProBuiltinNode {

    @Specialization
    @TruffleBoundary
    public Object executeGeneric(String symbolName, @CachedContext(PreProLanguage.class) PreProContext context) {
        return context.symbolExists(symbolName);
    }
}
