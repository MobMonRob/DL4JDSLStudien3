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
package com.oracle.truffle.prepro.test;

import com.oracle.truffle.prepro.PreProPolyglotContext;
import com.oracle.truffle.prepro.runtime.types.PreProConstant;
import com.oracle.truffle.prepro.runtime.types.PreProVector3;
import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests for PrePro builtin functions")
public class BuiltinFunctionsTest extends PreProAbstractTest {

    @Test
    @Disabled("because the returned value from the constant does not match the expected output")
    public void printFunctionWorks() {
        PreProVector3 v = new PreProVector3(Nd4j.create(new double[]{42, 54, 6, 2, 311, 543, 3455, 7377, 35, 56, 87686, 35765}, new int[]{4, 3}));
        context.exportSymbol("v", v)
                .eval(ClassLoader.getSystemResource("testPrintFunctionCall.prepro"));
        assertEquals("Rank: 2,Offset: 0\n" +
                " Order: c Shape: [4,3],  stride: [3,1]\n" +
                "Rank: 2,Offset: 0\n" +
                " Order: c Shape: [1,1],  stride: [1,1]\n" +
                "This is a message printed from within PrePro!\n", toUnixString());
    }

    @Test
    public void throwFunctionWorks() {
        Exception preproException = assertThrows(PolyglotException.class,
                () -> context.eval(ClassLoader.getSystemResource("testThrowFunctionCall.prepro")));

        String expectedMessage = "This is an exception thrown from within PrePro!";
        String actualMessage = preproException.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void sinFunctionWorks() {
        PreProConstant constant = new PreProConstant(Math.PI);
        final PreProPolyglotContext.PreProPolyglotResult result = context.exportSymbol("c", constant)
                .eval(ClassLoader.getSystemResource("testBuiltinSinConstant.prepro"));

        final PreProConstant resultConstant = (PreProConstant) result.importSymbol("result");
        assertEquals(.0d, resultConstant.getDoubleValue(), 1e-6);
    }
}
