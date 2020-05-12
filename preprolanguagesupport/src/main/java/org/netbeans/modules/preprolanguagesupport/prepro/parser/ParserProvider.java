package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.function.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProLexer;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProParser;

/**
 * Get antlr Parser and provide it with input
 * @author Paula
 */
public enum ParserProvider implements Function<String, PreProParser> {
    
    INSTANCE;

    @Override
    public PreProParser apply(String text) {
        CharStream input = CharStreams.fromString(text);
        Lexer lexer = new PreProLexer(input);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new PreProParser(tokenStream);
    }
}
