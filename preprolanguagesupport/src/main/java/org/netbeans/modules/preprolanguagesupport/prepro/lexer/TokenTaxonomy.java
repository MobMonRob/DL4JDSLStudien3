package org.netbeans.modules.preprolanguagesupport.prepro.lexer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProLexer;
import org.netbeans.modules.preprolanguagesupport.prepro.parser.NameMapping;


/**
 * Groups Tokens into groups of identical syntax Highlighting
 * @author paula
 */
public enum TokenTaxonomy {

    INSTANCE;

    private final List<PreproTokenId> tokens;

    private TokenTaxonomy() {
        tokens = new ArrayList<>();

        int max = PreProLexer.VOCABULARY.getMaxTokenType() + 1;
        for (int i = 1; i < max; i++) {
            PreproTokenId token = new PreproTokenId(NameMapping.map(i), getCategory(i), i);
            tokens.add(token);
        }
    }

    //map token to Category
    private String getCategory(int token) {
        Function<Integer, Category> mapping = t -> {
            if (t < PreProLexer.VEC3) {
                return Category.KEYWORD;
            } else if (t < PreProLexer.LPAREN) {
                return Category.TYPE;
            } else if (t < PreProLexer.ASSIGN) {
                return Category.SEPARATOR;
            } else if (t < PreProLexer.WS){
                return Category.OPERATOR;
            } else if (t == PreProLexer.WS){
                return Category.WS;
            } else if (t == PreProLexer.COMMENT || t == PreProLexer.LINE_COMMENT) {
                return Category.COMMENT;            
            } else if ( t == PreProLexer.CHAR_LITERAL || t == PreProLexer.STRING_LITERAL) {
                return Category.STRING;
            } else if (t <= PreProLexer.IDENTIFIER){
                return Category.VALUE;
            }            
            return Category.TEXT;
        
        };

        return mapping.apply(token).name();
    }

    public List<PreproTokenId> allTokens() {
        return tokens;
    }

    public List<PreproTokenId> tokens(Category category) {
        return tokens.stream().filter(t -> category.name().equals(t.primaryCategory())).collect(toList());
    }

    public Map<Integer, PreproTokenId> getIdTokenMap() {
        return tokens.stream().collect(toMap(PreproTokenId::ordinal, t -> t));
    }
}
