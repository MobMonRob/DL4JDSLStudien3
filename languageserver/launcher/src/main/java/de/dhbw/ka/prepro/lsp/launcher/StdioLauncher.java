package de.dhbw.ka.prepro.lsp.launcher;

import de.dhbw.ka.prepro.lsp.server.PreproLanguageServer;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.*;

public class StdioLauncher {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        disableLoggingOnStdio();
        startServer(System.in, System.out);
    }

    private static void disableLoggingOnStdio() throws IOException {
        LogManager.getLogManager().reset();
        final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }

        logger.setLevel(Level.ALL);
        logger.addHandler(new FileHandler("/development/studienarbeit/languageserver/output.log"));
    }

    private static void startServer(InputStream in, OutputStream out) throws InterruptedException, ExecutionException {
        final PreproLanguageServer preproLanguageServer = new PreproLanguageServer();
        final Launcher<LanguageClient> clientLauncher = LSPLauncher.createServerLauncher(preproLanguageServer, in, out);

        final LanguageClient remoteProxy = clientLauncher.getRemoteProxy();
        preproLanguageServer.connect(remoteProxy);

        clientLauncher.startListening().get();
    }
}
