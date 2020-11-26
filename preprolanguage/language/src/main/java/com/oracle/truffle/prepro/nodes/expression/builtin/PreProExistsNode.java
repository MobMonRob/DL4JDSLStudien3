package com.oracle.truffle.prepro.nodes.expression.builtin;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;

@NodeInfo(shortName = "exists")
public class PreProExistsNode extends PreProExpressionNode {

    private final FrameSlot slot;

    public PreProExistsNode(FrameSlot slot) {
        this.slot = slot;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        return ((slot != null) && frame.isObject(slot)) ? 1 : 0;
    }
}
