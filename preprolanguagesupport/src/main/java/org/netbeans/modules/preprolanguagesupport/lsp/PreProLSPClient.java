/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.preprolanguagesupport.lsp;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.lsp.client.spi.LanguageServerProvider;
//import org.netbeans.modules.preprolanguagesupport.prepro.FileType;
import org.openide.util.Lookup;

/**
 *
 * @author root
 */
//Uncomment to let the plugin talk to the LSP Server again
//@MimeRegistration(mimeType = FileType.MIME, service = LanguageServerProvider.class)
public class PreProLSPClient implements LanguageServerProvider {

    private static final Logger logger = Logger.getLogger("PreProLSPClient");
    
    @Override
    public LanguageServerDescription startServer(Lookup lkp) {
        try {
            //return getFromJar();
            return getFromSocket(8123);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not create ServerDescription", ex);
            return null;
        }
    }
    
    private LanguageServerDescription getFromJar() throws IOException {
        Process process = new ProcessBuilder("java", "-jar", "/development/studienarbeit/languageserver/launcher/target/preprolanguagelauncher.jar").start();
        return LanguageServerDescription.create(process.getInputStream(), process.getOutputStream(), process);
    }
    
    private LanguageServerDescription getFromSocket(int port) throws IOException {
        logger.log(Level.INFO, "Hello world Auf 127.0.0.1");
        
        final Socket socket = new Socket("127.0.0.1", port);
        return LanguageServerDescription.create(socket.getInputStream(), socket.getOutputStream(), null);
        
    }

}
