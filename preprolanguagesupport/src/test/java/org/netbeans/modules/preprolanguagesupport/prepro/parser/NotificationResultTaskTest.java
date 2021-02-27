/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.List;
import javax.swing.text.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.modules.preprolanguagesupport.prepro.PreProNode;
import org.netbeans.modules.preprolanguagesupport.prepro.parser.PreProProxyParser.PreProParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Source;

import static java.util.Collections.singletonList;
import org.junit.jupiter.api.Disabled;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@Disabled("Currently not working properly")
@ExtendWith(MockitoExtension.class)
public class NotificationResultTaskTest {

    @Mock
    private Snapshot snapshot;
    
    @Mock
    private Source source;
    
    @Mock
    private Document document;
    
    @Spy
    private NotificationResultTask classUnderTest;
    
    private PreProParserResult preproResult;
    
    @BeforeEach
    void setup() {
        List<PreProNode> res = singletonList(new PreProNode("foo", 0, 0));
        List<SyntaxError> errs = singletonList(new SyntaxError("bar", 0));
        preproResult = new PreProParserResult(snapshot, res, errs);
    }

    @Test
    @DisplayName("It should set errors in the HintsController")
    public void run() {
        given(snapshot.getSource()).willReturn(source);
        given(source.getDocument(false)).willReturn(document);
        
        classUnderTest.run(preproResult, null);
        
        verify(classUnderTest).setErrors(eq(document), any(List.class));
    }
    
    @Test
    @DisplayName("It should not set errors in the HintsController, when result is not finished.")
    public void run_noNotify() {
        preproResult.invalidate();
        classUnderTest.run(preproResult, null);
        
        verify(classUnderTest, never()).setErrors(eq(document), any(List.class));
    }
}
