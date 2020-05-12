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
import com.oracle.truffle.prepro.runtime.types.PreProScalar;
import com.oracle.truffle.prepro.runtime.types.PreProVector3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.factory.Nd4j;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Tests for PrePro Arithmetic")
public class PreProArithmeticTest extends PreProAbstractTest {

    @Test
    public void addConstantAndRawValue() {
        PreProConstant c = new PreProConstant(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("c", c)
                        .eval("function main() {export(\"result\", import(\"c\") + 1234567);}");
        PreProConstant returned = (PreProConstant) result.importSymbol("result");
        assertEquals(c.add(new PreProConstant(1234567)), returned);
    }

    @Test
    public void addConstants() {
        PreProConstant c1 = new PreProConstant(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProConstant c2 = new PreProConstant(Nd4j.create(new double[]{69875896}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("c1", c1)
                        .exportSymbol("c2", c2)
                        .eval("function main() {export(\"result\", import(\"c1\") + import(\"c2\"));}");
        PreProConstant returned = (PreProConstant) result.importSymbol("result");
        assertEquals(c1.add(c2), returned);
    }

    @Test
    public void addScalars() {
        PreProScalar s1 = new PreProScalar(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("s1", s1)
                        .eval("function main() {export(\"result\", import(\"s1\") + import(\"s1\") + import(\"s1\"));}");
        PreProScalar returned = (PreProScalar) result.importSymbol("result");
        assertEquals(s1.add(s1).add(s1), returned);
    }

    @Test
    public void addVectors() {
        PreProVector3 v1 = new PreProVector3(Nd4j.create(new double[]{42, 54, 6, 2, 311, 543, 3455, 7377, 35, 56, 87686, 35765}, new int[]{4, 3}));
        PreProVector3 v2 = new PreProVector3(Nd4j.create(new double[]{876, 563, 346, 343, 45643, 4265, 145, 657, 4365, 43526, 7675, 87}, new int[]{4, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("v1", v1)
                        .exportSymbol("v2", v2)
                        .eval("function main() {export(\"result\", import(\"v1\") + import(\"v2\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("result");
        assertEquals(v1.add(v2), returned);
    }

    @Test
    public void subVectors() {
        PreProVector3 v1 = new PreProVector3(Nd4j.create(new double[]{42, 54, 6, 2, 311, 543, 3455, 7377, 35, 56, 87686, 35765}, new int[]{4, 3}));
        PreProVector3 v2 = new PreProVector3(Nd4j.create(new double[]{876, 563, 346, 343, 45643, 4265, 145, 657, 4365, 43526, 7675, 87}, new int[]{4, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("v1", v1)
                        .exportSymbol("v2", v2)
                        .eval("function main() {export(\"result\", import(\"v1\") - import(\"v2\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("result");
        assertEquals(v1.sub(v2), returned);
    }

    @Test
    public void addMatrices() {
        PreProMatrix4 m1 = new PreProMatrix4(Nd4j.create(IntStream.range(1, 64 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 4, 4}));
        PreProMatrix4 m2 = new PreProMatrix4(Nd4j.create(IntStream.range(1, 64 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 4, 4}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("m1", m1)
                        .exportSymbol("m2", m2)
                        .eval("function main() {export(\"result\", import(\"m1\") + import(\"m2\"));}");
        PreProMatrix4 returned = (PreProMatrix4) result.importSymbol("result");
        assertEquals(m1.add(m2), returned);
    }

    @Test
    public void lazyMulConstants() {
        PreProConstant c1 = new PreProConstant(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProConstant c2 = new PreProConstant(Nd4j.create(new double[]{69875896}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("c1", c1)
                        .exportSymbol("c2", c2)
                        .eval("function main() {export(\"result\", import(\"c1\") ** import(\"c2\"));}");
        PreProConstant returned = (PreProConstant) result.importSymbol("result");
        assertEquals(c1.mul(c2), returned);
    }

    @Test
    public void mulScalars() {
        PreProScalar s1 = new PreProScalar(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProScalar s2 = new PreProScalar(Nd4j.create(new double[]{7462}, new int[]{1, 1}));
        PreProScalar s3 = new PreProScalar(Nd4j.create(new double[]{5.42}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("s1", s1)
                        .exportSymbol("s2", s2)
                        .exportSymbol("s3", s3)
                        .eval("function main() {export(\"result\", import(\"s1\") * import(\"s2\") * import(\"s3\"));}");
        PreProScalar returned = (PreProScalar) result.importSymbol("result");
        assertEquals(s1.mul(s2).mul(s3), returned);
    }

    @Test
    public void divScalars() {
        PreProScalar s1 = new PreProScalar(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProScalar s2 = new PreProScalar(Nd4j.create(new double[]{7462}, new int[]{1, 1}));
        PreProScalar s3 = new PreProScalar(Nd4j.create(new double[]{5.42}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("s1", s1)
                        .exportSymbol("s2", s2)
                        .exportSymbol("s3", s3)
                        .eval("function main() {export(\"result\", import(\"s1\") / import(\"s2\") / import(\"s3\"));}");
        PreProScalar returned = (PreProScalar) result.importSymbol("result");
        assertEquals(s1.div(s2).div(s3), returned);
    }

    @Test
    public void mulMatrices3() {
        PreProMatrix3 m1 = new PreProMatrix3(Nd4j.create(IntStream.range(1, 36 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 3, 3}));
        PreProMatrix3 m2 = new PreProMatrix3(Nd4j.create(IntStream.range(1, 36 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 3, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("m1", m1)
                        .exportSymbol("m2", m2)
                        .eval("function main() {export(\"result\", import(\"m1\") * import(\"m2\"));}");
        PreProMatrix3 returned = (PreProMatrix3) result.importSymbol("result");
        assertEquals(m1.mul(m2), returned);
    }

    @Test
    public void mulMatrixWithScalar() {
        PreProMatrix3 m = new PreProMatrix3(Nd4j.create(IntStream.range(1, 36 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 3, 3}));
        PreProScalar s = new PreProScalar(Nd4j.create(new double[]{1234567}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("m", m)
                        .exportSymbol("s", s)
                        .eval("function main() {export(\"result\", import(\"m\") * import(\"s\"));}");
        PreProMatrix3 returned = (PreProMatrix3) result.importSymbol("result");
        assertEquals(m.mul(s), returned);
    }

    @Test
    public void mulMatrixWithVector() {
        PreProMatrix3 m = new PreProMatrix3(Nd4j.create(IntStream.range(1, 36 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 3, 3}));
        PreProVector3 v = new PreProVector3(Nd4j.create(new double[]{876, 563, 346, 343, 45643, 4265, 145, 657, 4365, 43526, 7675, 87}, new int[]{4, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("m", m)
                        .exportSymbol("v", v)
                        .eval("function main() {export(\"result\", import(\"m\") * import(\"v\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("result");
        assertEquals(m.mul(v), returned);
    }

    @Test
    public void mulMatrices4() {
        PreProMatrix4 m1 = new PreProMatrix4(Nd4j.create(IntStream.range(1, 64 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 4, 4}));
        PreProMatrix4 m2 = new PreProMatrix4(Nd4j.create(IntStream.range(1, 64 + 1).mapToDouble(i -> i).toArray(), new int[]{4, 4, 4}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("m1", m1)
                        .exportSymbol("m2", m2)
                        .eval("function main() {export(\"result\", import(\"m1\") * import(\"m2\"));}");
        PreProMatrix4 returned = (PreProMatrix4) result.importSymbol("result");
        assertEquals(m1.mul(m2), returned);
    }

    @Test
    public void mulVectors() {
        PreProVector3 v1 = new PreProVector3(Nd4j.create(new double[]{42, 54, 6, 2, 311, 543, 3455, 7377, 35, 56, 87686, 35765}, new int[]{4, 3}));
        PreProVector3 v2 = new PreProVector3(Nd4j.create(new double[]{876, 563, 346, 343, 45643, 4265, 145, 657, 4365, 43526, 7675, 87}, new int[]{4, 3}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("v1", v1)
                        .exportSymbol("v2", v2)
                        .eval("function main() {export(\"result\", import(\"v1\") * import(\"v2\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("result");
        assertEquals(v1.mul(v2), returned);
    }

    @Test
    public void mulVectorWithScalar() {
        PreProVector3 v = new PreProVector3(Nd4j.create(new double[]{42, 54, 6, 2, 311, 543, 3455, 7377, 35, 56, 87686, 35765}, new int[]{4, 3}));
        PreProScalar s = new PreProScalar(Nd4j.create(new double[]{1_234_567}, new int[]{1, 1}));
        PreProPolyglotContext.PreProPolyglotResult result =
                context.exportSymbol("v", v)
                        .exportSymbol("s", s)
                        .eval("function main() {export(\"result\", import(\"v\") * import(\"s\"));}");
        PreProVector3 returned = (PreProVector3) result.importSymbol("result");
        assertEquals(v.mul(s), returned);
    }
}
