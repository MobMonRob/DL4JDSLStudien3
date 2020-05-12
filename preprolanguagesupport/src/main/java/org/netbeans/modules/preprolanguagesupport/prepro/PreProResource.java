
package org.netbeans.modules.preprolanguagesupport.prepro;

/**
 * 
 * @author paula
 */
public class PreProResource {

    private final String name;
    private final int type;
    private final int offset;

    public PreProResource(String name, int type, int offset) {
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
