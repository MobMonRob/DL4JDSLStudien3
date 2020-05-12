package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.List;
import java.util.function.Function;
import javax.swing.event.ChangeListener;

import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.netbeans.modules.preprolanguagesupport.prepro.PreProResource;
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
        //do the parsing
        preProParser.prepro();

        List<PreProResource> resources = listener.getResources();
        List<SyntaxError> errors = errorListener.getSyntaxErrors();

        parserResult = new PreProParserResult(snapshot, resources, errors);
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
        private final List<PreProResource> resources;
        private final List<SyntaxError> errors;

        public PreProParserResult(Snapshot snapshot, List<PreProResource> resources, List<SyntaxError> errors) {
            super(snapshot);
            this.resources = resources;
            this.errors = errors;
        }

        public List<PreProResource> getResources() {
            return resources;
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
