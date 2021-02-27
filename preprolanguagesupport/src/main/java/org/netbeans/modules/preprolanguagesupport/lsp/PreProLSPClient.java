/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.preprolanguagesupport.lsp;

import java.io.IOException;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.lsp.client.spi.LanguageServerProvider;
import org.openide.util.Lookup;

/**
 *
 * @author root
 */
@MimeRegistration(mimeType = org.netbeans.modules.preprolanguagesupport.prepro.FileType.MIME, service = LanguageServerProvider.class)
public class PreProLSPClient implements LanguageServerProvider {

    @Override
    public LanguageServerDescription startServer(Lookup lkp) {
        try {
            Process process = new ProcessBuilder("java", "-jar", "/development/studienarbeit/languageserver/launcher/target/preprolanguagelauncher.jar").start();
            return LanguageServerDescription.create(process.getInputStream(), process.getOutputStream(), process);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
