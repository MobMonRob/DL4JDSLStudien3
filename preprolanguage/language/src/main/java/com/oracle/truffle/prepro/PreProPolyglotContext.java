package com.oracle.truffle.prepro;

import com.oracle.truffle.prepro.runtime.PreProContext;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.Source;

import java.io.OutputStream;
import java.net.URL;

public final class PreProPolyglotContext {

    private static final String ID = PreProLanguage.ID;
    private Context languageContext;
    private PreProContext runtimeContext;

    public PreProPolyglotContext(OutputStream printOut) {
        languageContext = Context.newBuilder()
                .allowPolyglotAccess(PolyglotAccess.ALL)
                .out(null != printOut ? printOut : System.out).build();
        languageContext.initialize(ID);
        languageContext.enter();
        runtimeContext = PreProLanguage.getCurrentContext();
    }

    public PreProPolyglotContext exportSymbol(String symbolName, Object value) {
        runtimeContext.exportSymbol(symbolName, value);
        return this;
    }

    public PreProPolyglotResult eval(String literalPreProProgram) {
        languageContext.eval(ID, literalPreProProgram);
        return new PreProPolyglotResult();
    }

    public PreProPolyglotResult eval(URL preProFileUrl) {
        languageContext.eval(Source.newBuilder(ID, preProFileUrl).buildLiteral());
        return new PreProPolyglotResult();
    }

    public final void cleanup() {
        if (languageContext != null) {
            languageContext.leave();
            languageContext.close();
        }
    }

    public final class PreProPolyglotResult {

        private PreProPolyglotResult() {
        }

        public Object importSymbol(String symbolName) {
            return runtimeContext.importSymbol(symbolName);
        }
    }
}
