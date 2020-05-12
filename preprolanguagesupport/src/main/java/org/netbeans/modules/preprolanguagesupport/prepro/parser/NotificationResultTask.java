package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.List;
import javax.swing.text.Document;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.ParserResultTask;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;
import org.netbeans.spi.editor.hints.ErrorDescription;
import org.netbeans.spi.editor.hints.ErrorDescriptionFactory;
import org.netbeans.spi.editor.hints.HintsController;
import org.netbeans.spi.editor.hints.Severity;

import static java.util.stream.Collectors.toList;
import org.netbeans.modules.preprolanguagesupport.LookupContext;

/**
 * Sets syntax errors in opened Document
 * @author Paula
 */
public class NotificationResultTask extends ParserResultTask {

    private static final String LAYER = "prepro";

    @Override
    public void run(Parser.Result result, SchedulerEvent se) {
        PreProProxyParser.PreProParserResult preProResult = (PreProProxyParser.PreProParserResult) result;

        if (preProResult.isValid()) {

            LookupContext.INSTANCE.add(preProResult.getResources());

            Document document = result.getSnapshot().getSource().getDocument(false);
            List<SyntaxError> errors = preProResult.getErrors();
            List<ErrorDescription> descriptions = errors.stream().map(e
                    -> ErrorDescriptionFactory.createErrorDescription(
                            Severity.ERROR,
                            e.getMessage(),
                            document,
                            e.getLine())).collect(toList());
            setErrors(document, descriptions);
        }
    }

    void setErrors(Document document, List<ErrorDescription> descriptions) {
        HintsController.setErrors(document, LAYER, descriptions);
    }

    @Override
    public int getPriority() {
        return 100; //the lower, the higher the priority
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.EDITOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {
    }
}
