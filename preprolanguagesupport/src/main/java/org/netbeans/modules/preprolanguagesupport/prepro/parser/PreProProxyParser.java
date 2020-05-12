package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.List;
import java.util.function.Function;
import javax.swing.event.ChangeListener;

import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.netbeans.modules.preprolanguagesupport.prepro.PreProNode;
import org.netbeans.modules.preprolanguagesupport.prepro.grammar.PreProParser;

/**
 * Extends Netbeans Parser and hands input to antlr parser
 * @author paula
 */
public class PreProProxyParser extends Parser {

    private final Function<String, PreProParser> parserProvider;

    private PreProParserResult parserResult;

    public PreProProxyParser(Function<String, PreProParser> parserProvider) {
        this.parserProvider = parserProvider;
    }

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent sme) throws ParseException {

        String text = snapshot.getText().toString();
        PreProParser preProParser = parserProvider.apply(text);

        ParserListener listener = new ParserListener();
        ErrorParserListener errorListener = new ErrorParserListener();
        preProParser.addParseListener(listener);
        preProParser.addErrorListener(errorListener);
        // let antlr generated Parser parse editor input
        preProParser.prepro();
        // get currently parsed section of PrePro
        List<PreProNode> nodes = listener.getNodes();
        // retrieve syntax errors from antlr Listener
        List<SyntaxError> errors = errorListener.getSyntaxErrors();
        // combine everything to PreProParserResult
        parserResult = new PreProParserResult(snapshot, nodes, errors);
    }

    @Override
    public Result getResult(Task task) throws ParseException {
        return parserResult;
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }

    public static class PreProParserResult extends Parser.Result {

        private boolean valid = true;
        private final List<PreProNode> nodes;
        private final List<SyntaxError> errors;

        public PreProParserResult(Snapshot snapshot, List<PreProNode> nodes, List<SyntaxError> errors) {
            super(snapshot);
            this.nodes = nodes;
            this.errors = errors;
        }

        public List<PreProNode> getResources() {
            return nodes;
        }

        public List<SyntaxError> getErrors() {
            return errors;
        }

        @Override
        protected void invalidate() {
            valid = false;
        }

        public boolean isValid() {
            return valid;
        }
    }
}
