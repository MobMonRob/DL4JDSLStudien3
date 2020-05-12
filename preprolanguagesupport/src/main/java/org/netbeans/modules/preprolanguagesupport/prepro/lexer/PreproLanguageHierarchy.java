package org.netbeans.modules.preprolanguagesupport.prepro.lexer;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.netbeans.modules.preprolanguagesupport.prepro.FileType;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Creates new Lexer for PrePro and contains all PreProTokens
 * @author paula
 */
public class PreproLanguageHierarchy extends LanguageHierarchy<PreproTokenId> {


    private final List<PreproTokenId> tokens;
    private final Map<Integer, PreproTokenId> idToToken;

    public PreproLanguageHierarchy() {
        tokens = TokenTaxonomy.INSTANCE.allTokens();
        idToToken = TokenTaxonomy.INSTANCE.getIdTokenMap();
    }

    @Override
    protected Collection<PreproTokenId> createTokenIds() {
        return tokens;
    }

    @Override
    protected Lexer<PreproTokenId> createLexer(LexerRestartInfo<PreproTokenId> info) {
        return new PreproEditorLexer(info, idToToken);
    }

    @Override
    protected String mimeType() {
        return FileType.MIME;
    }
}
