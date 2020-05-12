package org.netbeans.modules.preprolanguagesupport.prepro.parser;

import java.util.Collection;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.ParserFactory;
import org.netbeans.modules.preprolanguagesupport.prepro.FileType;


/**
 * Register Parser for PrePro
 * @author Paula
 */
@MimeRegistration(mimeType = FileType.MIME, service = ParserFactory.class)
public class PreProParserFactory extends ParserFactory {

    @Override
    public Parser createParser(Collection<Snapshot> coll) {
        return new PreProProxyParser(ParserProvider.INSTANCE);
    }
}
