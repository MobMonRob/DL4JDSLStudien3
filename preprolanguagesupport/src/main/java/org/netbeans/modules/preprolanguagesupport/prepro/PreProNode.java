
package org.netbeans.modules.preprolanguagesupport.prepro;

/**
 * Corresponds to the currently parsed node of parse tree
 * @author paula
 */
public class PreProNode {

    private final String name;
    private final int type;
    private final int offset;

    public PreProNode(String name, int type, int offset) {
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    
    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }
}
