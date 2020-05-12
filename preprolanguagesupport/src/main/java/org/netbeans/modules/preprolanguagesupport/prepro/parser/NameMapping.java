package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProLexer;


/**
 * Prepares display of syntax error
 * @author paula
 */
public interface NameMapping {
    public static String map(int type) {
        String name = PreProLexer.VOCABULARY.getDisplayName(type);
        return name.replaceAll("^\\'|\\'$", "");
    }
}
