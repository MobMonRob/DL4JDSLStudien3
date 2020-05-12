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
import com.oracle.truffle.prepro.runtime.types.PreProScalar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;

import static com.oracle.truffle.prepro.runtime.types.PreProConstant.FALSE;
import static com.oracle.truffle.prepro.runtime.types.PreProConstant.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for PrePro Logic")
public class PreProLogicTest extends PreProAbstractTest {

    @Test
    public void testComparisonOperationsOnScalars() {
        PreProScalar s1 = new PreProScalar(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProScalar s2 = new PreProScalar(Nd4j.create(new double[]{7462}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("s1", s1)
                        .exportSymbol("s2", s2)
                        .eval(ClassLoader.getSystemResource("testComparisonsOnScalar.prepro"));
        PreProScalar equal = (PreProScalar) result.importSymbol("equal");
        PreProScalar less = (PreProScalar) result.importSymbol("less");
        PreProScalar lessOrEqual = (PreProScalar) result.importSymbol("lessOrEqual");
        PreProScalar greater = (PreProScalar) result.importSymbol("greater");
        PreProScalar greaterOrEqual = (PreProScalar) result.importSymbol("greaterOrEqual");
        assertEquals(s1.isEqualTo(s2), equal);
        assertEquals(s1.isLessThan(s2), less);
        assertEquals(s1.isLessOrEqualThan(s2), lessOrEqual);
        assertEquals(s1.isGreaterThan(s2), greater);
        assertEquals(s1.isGreaterOrEqualThan(s2), greaterOrEqual);
    }

    @Test
    public void testLogicOperationsOnConstants() {
        PreProPolyglotContext.PreProPolyglotResult result =
                context.eval(ClassLoader.getSystemResource("testLogicOnConstant.prepro"));
        PreProConstant and1 = (PreProConstant) result.importSymbol("and1");
        PreProConstant or1 = (PreProConstant) result.importSymbol("or1");
        PreProConstant and2 = (PreProConstant) result.importSymbol("and2");
        PreProConstant or2 = (PreProConstant) result.importSymbol("or2");
        PreProConstant and3 = (PreProConstant) result.importSymbol("and3");
        PreProConstant or3 = (PreProConstant) result.importSymbol("or3");
        PreProConstant res = (PreProConstant) result.importSymbol("res");
        assertEquals(TRUE.getDoubleValue(), and1.getDoubleValue());
        assertEquals(TRUE.getDoubleValue(), or1.getDoubleValue());
        assertEquals(FALSE.getDoubleValue(), and2.getDoubleValue());
        assertEquals(TRUE.getDoubleValue(), or2.getDoubleValue());
        assertEquals(FALSE.getDoubleValue(), and3.getDoubleValue());
        assertEquals(TRUE.getDoubleValue(), or3.getDoubleValue());
        assertEquals(TRUE.getDoubleValue(), res.getDoubleValue());
    }
}
