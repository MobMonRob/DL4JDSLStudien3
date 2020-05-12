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
import com.oracle.truffle.prepro.runtime.types.PreProMatrix3;
import com.oracle.truffle.prepro.runtime.types.PreProMatrix4;
import com.oracle.truffle.prepro.runtime.types.PreProVector3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests if PrePro Types can all be used")
public class UsePreProTypesTest extends PreProAbstractTest {

    @Test
    public void importPreProConstant() {
        PreProConstant entered = new PreProConstant(Nd4j.create(new double[]{42}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("imported", entered)
                        .eval("function main() {export(\"exported\", import(\"imported\"));}");
        PreProConstant returned = (PreProConstant) result.importSymbol("exported");
        assertEquals(entered, returned);
    }

    @Test
    public void importPreProVector3() {
        PreProVector3 entered = new PreProVector3(Nd4j.create(new double[]{42, 0, 1, 1, 0, 1, 5, 0, 1, 6, 0, 1}, new int[]{4, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("imported", entered)
                        .eval("function main() {export(\"exported\", import(\"imported\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("exported");
        assertEquals(entered, returned);
    }

    @Test
    public void importPreProMatrix3() {
        PreProMatrix3 entered = new PreProMatrix3(Nd4j.create(IntStream.range(1, 36 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 3, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("imported", entered)
                        .eval("function main() {export(\"exported\", import(\"imported\"));}");
        PreProMatrix3 returned = (PreProMatrix3) result.importSymbol("exported");
        assertEquals(entered, returned);
    }

    @Test
    public void importPreProMatrix4() {
        PreProMatrix4 entered = new PreProMatrix4(Nd4j.create(IntStream.range(1, 64 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 4, 4}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("imported", entered)
                        .eval("function main() {export(\"exported\", import(\"imported\"));}");
        PreProMatrix4 returned = (PreProMatrix4) result.importSymbol("exported");
        assertEquals(entered, returned);
    }
}
