/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.netbeans.modules.preprolanguagesupport.prepro.parser.PreProProxyParser.PreProParserResult;

@ExtendWith(MockitoExtension.class)
public class PreproProxyParserTest {
    
   
    private static final String TEXT = "function main(){}";
    
    @Mock
    private Snapshot snapshot;
    
    @Mock
    private Task task;
    
    @Mock
    private SourceModificationEvent event;

    private PreProProxyParser classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new PreProProxyParser(ParserProvider.INSTANCE);
        given(snapshot.getText()).willReturn(TEXT);
    }

    /**
     * Test of parse method, of class PreProProxyParser.
     */
    @Test
    @DisplayName("The parse method should produce a result")
    public void parse() throws Exception {
        classUnderTest.parse(snapshot, task, event);
        Parser.Result result = classUnderTest.getResult(task);
        assertThat(result instanceof PreProParserResult, is(true));
    }
}

    

