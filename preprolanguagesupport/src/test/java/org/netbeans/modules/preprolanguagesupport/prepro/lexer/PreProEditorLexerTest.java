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
package org.netbeans.modules.preprolanguagesupport.prepro.lexer;

import java.util.Map;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.api.lexer.Token;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;

import static java.util.Collections.singletonMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Disabled;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@Disabled("Currently not working properly")
@ExtendWith(MockitoExtension.class)
public class PreProEditorLexerTest {
    
    @Mock
    private LexerRestartInfo<PreproTokenId> info;
    
    @Mock
    private TokenFactory tokenFactory;
    
    @Mock
    private Supplier<org.antlr.v4.runtime.Token> tokenSupplier;
    
    @Mock 
    private org.antlr.v4.runtime.Token antlrToken;
    
    @Mock
    private Token<PreproTokenId> token;
    
    private PreproEditorLexer classUnterTest;
    
    @BeforeEach
    void setUp() {
        given(info.tokenFactory()).willReturn(tokenFactory);
        given(tokenSupplier.get()).willReturn(antlrToken);
        
        Map<Integer, PreproTokenId> map = singletonMap(0, new PreproTokenId("foo", Category.KEYWORD.name(), 10));
        classUnterTest = new PreproEditorLexer(info, map, tokenSupplier);
    }

    @Test
    @DisplayName("The lexer should return a token.")
    public void testNextToken() {
        given(tokenFactory.createToken(any(PreproTokenId.class))).willReturn(token);
        Token<PreproTokenId> result = classUnterTest.nextToken();
        assertThat(result, equalTo(token));
    }
}
