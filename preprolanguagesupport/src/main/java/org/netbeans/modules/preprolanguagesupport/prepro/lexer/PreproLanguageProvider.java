package org.netbeans.modules.preprolanguagesupport.prepro.lexer;

import java.util.function.Supplier;
import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.modules.preprolanguagesupport.prepro.FileType;
import org.netbeans.spi.lexer.LanguageEmbedding;
import org.netbeans.spi.lexer.LanguageProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 * Deliver the Language
 * @author paula
 */
@ServiceProvider(service=LanguageProvider.class)
public class PreproLanguageProvider extends LanguageProvider{
    
    private final Supplier<Language<?>> supplier = () -> new PreproLanguageHierarchy().language();

    @Override
    public Language<?> findLanguage(String mime) {
        return (FileType.MIME.equals(mime)) ? supplier.get() : null;
    }

    @Override
    public LanguageEmbedding<?> findLanguageEmbedding(Token<?> token, LanguagePath lp, InputAttributes ia) {
        return null;
    }
    
}
