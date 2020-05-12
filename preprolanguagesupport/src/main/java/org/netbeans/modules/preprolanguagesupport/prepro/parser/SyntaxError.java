package org.netbeans.modules.preprolanguagesupport.prepro.parser;


/**
 * Defines PrePro Syntax Errors
 * @author Paula
 */
public final class SyntaxError {

    private final String message;
    private final int line;

    public SyntaxError(String message, int line) {
        this.message = message;
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }
}
