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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.netbeans.api.lexer.Language;
import org.netbeans.modules.preprolanguagesupport.prepro.FileType;


public class PreproLanguageProviderTest {

    private PreproLanguageProvider classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new PreproLanguageProvider();
    }

    /**
     * Test of findLanguage method, of class PreProLanguageProvider.
     */
    @Test
    @DisplayName("It should return null when the mime is null.")
    public void findLanguage_MimeIsNull() {
        Language result = classUnderTest.findLanguage(null);
        assertThat(result, nullValue());
    }

    /**
     * Test of findLanguage method, of class PreProLanguageProvider.
     */
    @Test
    @DisplayName("It should return PreproLanguage when the mime is prepro.")
    public void findLanguage_MimeIsPrePro() {
        String mime = FileType.MIME;
        Language result = classUnderTest.findLanguage(mime);
        assertThat(result.mimeType(), equalTo(mime));
    }

}
