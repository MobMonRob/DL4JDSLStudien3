
package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * Listens to syntax errors in antlr Parser
 * @author paula
 */
public class ErrorParserListener extends BaseErrorListener{
    
    private final List<SyntaxError> syntaxErrors = new ArrayList<>();

    public List<SyntaxError> getSyntaxErrors() {
        return syntaxErrors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String message, RecognitionException e) {
        // at the moment we only care for error message and line
        syntaxErrors.add(new SyntaxError(message, line));
    }
}
