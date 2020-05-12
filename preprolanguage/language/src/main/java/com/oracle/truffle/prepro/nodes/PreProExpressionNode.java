/*
 * Copyright (c) 2012, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.prepro.nodes;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.ProbeNode;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.instrumentation.Tag;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.prepro.runtime.types.PreProConstant;
import com.oracle.truffle.prepro.runtime.types.PreProMatrix;
import com.oracle.truffle.prepro.runtime.types.PreProMatrix3;
import com.oracle.truffle.prepro.runtime.types.PreProMatrix4;
import com.oracle.truffle.prepro.runtime.types.PreProScalar;
import com.oracle.truffle.prepro.runtime.types.PreProVector;
import com.oracle.truffle.prepro.runtime.types.PreProVector3;
import com.oracle.truffle.prepro.runtime.types.PreProVector4;

/**
 * Base class for all PrePro nodes that produce a value and therefore benefit from type specialization.
 * The annotation {@link TypeSystemReference} specifies the PrePro types. Specifying it here defines the
 * type system for all subclasses.
 */
@TypeSystemReference(PreProTypes.class)
@NodeInfo(description = "The abstract base node for all expressions")
@GenerateWrapper
public abstract class PreProExpressionNode extends PreProStatementNode {

    private boolean hasExpressionTag;

    /**
     * The execute method when no specialization is possible. This is the most general case,
     * therefore it must be provided by all subclasses.
     */
    public abstract Object executeGeneric(VirtualFrame frame);

    /**
     * When we use an expression at places where a {@link PreProStatementNode statement} is already
     * sufficient, the return value is just discarded.
     */
    @Override
    public void executeVoid(VirtualFrame frame) {
        executeGeneric(frame);
    }

    @Override
    public WrapperNode createWrapper(ProbeNode probe) {
        return new PreProExpressionNodeWrapper(this, probe);
    }

    @Override
    public boolean hasTag(Class<? extends Tag> tag) {
        if (tag == StandardTags.ExpressionTag.class) {
            return hasExpressionTag;
        }
        return super.hasTag(tag);
    }

    /**
     * Marks this node as being a {@link StandardTags.ExpressionTag} for instrumentation purposes.
     */
    public final void addExpressionTag() {
        hasExpressionTag = true;
    }

    /*
     * Execute methods for specialized types. They all follow the same pattern: they call the
     * generic execution method and then expect a result of their return type. Type-specialized
     * subclasses overwrite the appropriate methods.
     */

    public double executeDouble(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectDouble(executeGeneric(frame));
    }

    public PreProConstant executePreProConstant(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProConstant(executeGeneric(frame));
    }

    public PreProScalar executePreProScalar(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProScalar(executeGeneric(frame));
    }

    public PreProVector4 executePreProVector4(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProVector4(executeGeneric(frame));
    }

    public PreProVector3 executePreProVector3(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProVector3(executeGeneric(frame));
    }

    public PreProVector executePreProVector(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProVector(executeGeneric(frame));
    }

    public PreProMatrix4 executePreProMatrix4(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProMatrix4(executeGeneric(frame));
    }

    public PreProMatrix3 executePreProMatrix3(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProMatrix3(executeGeneric(frame));
    }

    public PreProMatrix executePreProMatrix(VirtualFrame frame) throws UnexpectedResultException {
        return PreProTypesGen.expectPreProMatrix(executeGeneric(frame));
    }
}
