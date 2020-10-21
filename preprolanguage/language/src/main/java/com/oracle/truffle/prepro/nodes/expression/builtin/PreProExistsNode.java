package com.oracle.truffle.prepro.nodes.expression.builtin;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;

@NodeInfo(shortName = "exists")
public class PreProExistsNode extends PreProExpressionNode {

    private final String text;

    public PreProExistsNode(String text) {
        this.text = text;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (frame.getFrameDescriptor().findFrameSlot(text) != null) {
            return 1;
        }
        return 0;
    }
}
