package org.netbeans.modules.preprolanguagesupport.prepro.lexer;

import org.netbeans.api.lexer.TokenId;

/**
 * Defines PreproTokens
 * @author paula
 */
public class PreproTokenId implements TokenId{
    
    private final String name;
    private final String primaryCategory;
    private final int id;
    
    public PreproTokenId(String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public String toString() {
        return "PreproTokenId{" + "name=" + name + ", primaryCategory=" + primaryCategory + ", id=" + id + '}';
    }

}
