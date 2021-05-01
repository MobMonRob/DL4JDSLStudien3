/*
 * Copyright (c) 2012, 2018, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.prepro.nodes.local;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.GenerateWrapper;
import com.oracle.truffle.api.instrumentation.InstrumentableNode;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.prepro.PreProException;
import com.oracle.truffle.prepro.nodes.PreProExpressionNode;
import com.oracle.truffle.prepro.nodes.PreProStatementNode;
import com.oracle.truffle.prepro.nodes.util.PreProUnboxNodeGen;
import com.oracle.truffle.prepro.runtime.types.VariableType;

/**
 * Node to write a local variable to a function's {@link VirtualFrame frame}. The Truffle frame API
 * allows to store primitive values of all Java primitive types, and Object values.
 */
@GenerateWrapper
@NodeInfo(shortName = "assign", description = "The node implementing an assignment statement")
public final class PreProWriteLocalVariableNode extends PreProStatementNode{

    /**
     * Value to assign the variable. Since PrePro is statically typed, {@link #executeVoid}
     * will throw a type assignment error if types are not compatible.
     */
    @Child
    private PreProExpressionNode valueNode;

    /**
     * The type of the variable.
     */
    private String type;

    private FrameSlot frameSlot;

    public PreProWriteLocalVariableNode(FrameSlot frameSlot, String type, PreProExpressionNode valueNode) {
        this.frameSlot = frameSlot;
        this.type = type;
        this.valueNode = PreProUnboxNodeGen.create(valueNode);
    }

    public PreProWriteLocalVariableNode(FrameSlot frameSlot, PreProExpressionNode valueNode) {
        this.frameSlot = frameSlot;
        this.valueNode = PreProUnboxNodeGen.create(valueNode);
    }

    @Override
    public void executeVoid(VirtualFrame frame) {
        try {
            if (null == type) {
                writeToExisting(frame);
            } else {
                writeToNew(frame);
            }
        } catch (UnexpectedResultException | FrameSlotTypeException e) {
            throw PreProException.assignmentError(this, type);
        }
    }

    private void writeToNew(VirtualFrame frame) throws UnexpectedResultException {
        TruffleObject value;
        switch (VariableType.getTypeForText(type)) {
            case VEC3:
                value = valueNode.executePreProVector3(frame);
                break;
            case VEC4:
                value = valueNode.executePreProVector4(frame);
                break;
            case MAT:
                value = valueNode.executePreProMatrix(frame);
                break;
            case MAT3:
                value = valueNode.executePreProMatrix3(frame);
                break;
            case MAT4:
                value = valueNode.executePreProMatrix4(frame);
                break;
            case SCAL:
                value = valueNode.executePreProScalar(frame);
                break;
            case CONSTANT:
                value = valueNode.executePreProConstant(frame);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + VariableType.getTypeForText(type));
        }
        frame.getFrameDescriptor().setFrameSlotKind(frameSlot, FrameSlotKind.Object);
        frame.setObject(frameSlot, value);
    }

    private void writeToExisting(VirtualFrame frame) throws FrameSlotTypeException, UnexpectedResultException {
        Object value = frame.getObject(frameSlot);
        switch (VariableType.getTypeForClass(value.getClass())) {
            case VEC3:
                value = valueNode.executePreProVector3(frame);
                break;
            case VEC4:
                value = valueNode.executePreProVector4(frame);
                break;
            case MAT:
                value = valueNode.executePreProMatrix(frame);
                break;
            case MAT3:
                value = valueNode.executePreProMatrix3(frame);
                break;
            case MAT4:
                value = valueNode.executePreProMatrix4(frame);
                break;
            case SCAL:
                value = valueNode.executePreProScalar(frame);
                break;
            case CONSTANT:
                value = valueNode.executePreProConstant(frame);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + VariableType.getTypeForClass(value.getClass()));
        }
        frame.setObject(frameSlot, value);
    }

    FrameSlot getSlot() {
        return this.frameSlot;
    }
}
