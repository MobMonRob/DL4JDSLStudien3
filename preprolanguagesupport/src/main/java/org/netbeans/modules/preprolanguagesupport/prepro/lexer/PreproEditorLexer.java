package org.netbeans.modules.preprolanguagesupport.prepro.lexer;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import org.antlr.v4.runtime.CharStream;
import org.netbeans.api.lexer.Token;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProLexer;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Get Input from NetbeansEditor
 * @author paula
 */
public final class PreproEditorLexer implements Lexer<PreproTokenId> {

    private final LexerRestartInfo<PreproTokenId> info;
    private final Map<Integer, PreproTokenId> idToToken;

    private final Function<PreproTokenId, Token<PreproTokenId>> tokenFactory;
    private final Supplier<org.antlr.v4.runtime.Token> tokenSupplier;

    public PreproEditorLexer(LexerRestartInfo<PreproTokenId> info, Map<Integer, PreproTokenId> idToToken) {
        this(info, idToToken, new TokenSupplier(info.input()));
    }
    
    PreproEditorLexer(LexerRestartInfo<PreproTokenId> info, Map<Integer, PreproTokenId> idToToken, 
            Supplier<org.antlr.v4.runtime.Token> tokenSupplier) {
        this.info = info;
        this.idToToken = idToToken;
        this.tokenSupplier = tokenSupplier;
        this.tokenFactory = id -> info.tokenFactory().createToken(id);
    }

    @Override
    public Token<PreproTokenId> nextToken() {
        Token<PreproTokenId> createdToken = null;

        org.antlr.v4.runtime.Token token = tokenSupplier.get();

        int type = token.getType();
        if (type != -1) {
            createdToken = createToken(type);
        } else if (info.input().readLength() > 0) {
            createdToken = createToken(PreProLexer.WS);
        }

        return createdToken;
    }

    private Token<PreproTokenId> createToken(int type) {
        Function<Integer, PreproTokenId> mapping = idToToken::get;
        return mapping.andThen(tokenFactory).apply(type);
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
        //not implemented
    }

    private static class TokenSupplier implements Supplier<org.antlr.v4.runtime.Token> {

        private final PreProLexer lexer;
        // tunnel editor Input to antlr
        TokenSupplier(LexerInput input) {
            CharStream stream = new LexerCharStream(input);
            lexer = new PreProLexer(stream);
        }

        @Override
        public org.antlr.v4.runtime.Token get() {
            return lexer.nextToken();
        }

    }
}
