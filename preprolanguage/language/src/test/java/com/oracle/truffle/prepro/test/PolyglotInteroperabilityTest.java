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

import com.oracle.truffle.prepro.PreProPolyglotContext.PreProPolyglotResult;
import com.oracle.truffle.prepro.runtime.types.PreProConstant;
import com.oracle.truffle.prepro.runtime.types.PreProVector3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for Java <-> PrePro Polyglot Interoperability")
public class PolyglotInteroperabilityTest extends PreProAbstractTest {

    @Test
    public void interopWorksSimple() {
        PreProConstant entered = new PreProConstant(Nd4j.create(new double[]{42}, new int[]{1, 1}));
        PreProPolyglotResult result =
                context.exportSymbol("imported", entered)
                        .eval("function main() {export(\"exported\", import(\"imported\"));}");
        PreProConstant returned = (PreProConstant) result.importSymbol("exported");
        assertEquals(entered, returned);
    }

    @Test
    public void interopWorksComplex() {
        PreProVector3 vector3One = new PreProVector3(Nd4j.create(new double[]{42, 0, 0, 1, 0, 0, 5, 0, 0, 6, 0, 0, 1, 2, 3}, new int[]{4, 3}));
        PreProVector3 vector3Two = new PreProVector3(Nd4j.create(new double[]{43, 0, 0, 44_662, 0, 0, 6, 0, 0, 7, 0, 0}, new int[]{4, 3}));
        PreProVector3 vector3Three = new PreProVector3(Nd4j.create(new double[]{42, 0, 1, 1, 0, 1, 5, 0, 1, 6, 0, 1}, new int[]{4, 3}));
        PreProPolyglotResult result =
                context.exportSymbol("vector3One", vector3One)
                        .exportSymbol("vector3Two", vector3Two)
                        .exportSymbol("vector3Three", vector3Three)
                        .eval("function main() {" +
                                "vec3 p1 = import(\"vector3One\");" +
                                "vec3 x = calculateDifference(p1, import(\"vector3Two\"));" +
                                "vec3 s = calculateDifference(p1, import(\"vector3Three\"));" +
                                "vec3 y = s X x;" +
                                "vec3 z = y X x;" +
                                "export(\"x\", x); export(\"y\", y); export(\"z\", z);" +
                                "}" +
                                "function calculateDifference(vec3 p1, vec3 p2) returns vec3 {" +
                                "return p2 - p1;" +
                                "}");
        PreProVector3 x = (PreProVector3) result.importSymbol("x");
        PreProVector3 y = (PreProVector3) result.importSymbol("y");
        PreProVector3 z = (PreProVector3) result.importSymbol("z");
        assertEquals(Nd4j.create(new double[]{1, 0, 0, 44_661, 0, 0, 1, 0, 0, 1, 0, 0}, new int[]{4, 3}), x.timeSeries());
        assertEquals(Nd4j.create(new double[]{0, 1, 0, 0, 44_661, 0, 0, 1, 0, 0, 1, 0}, new int[]{4, 3}), y.timeSeries());
        assertEquals(Nd4j.create(new double[]{0, 0, -1, 0, 0, -1_994_604_928, 0, 0, -1, 0, 0, -1}, new int[]{4, 3}), z.timeSeries());
    }
}
