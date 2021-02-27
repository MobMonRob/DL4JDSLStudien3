package de.dhbw.ka.prepro.lsp.server;

import de.dhbw.ka.prepro.lsp.server.information.ServerInformation;
import de.dhbw.ka.prepro.lsp.server.services.PreproTextDocumentService;
import de.dhbw.ka.prepro.lsp.server.services.PreproWorkspaceService;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Nonnull
public class PreproLanguageServer implements LanguageServer, LanguageClientAware {
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    
    private final PreproTextDocumentService textDocumentService;
    private final PreproWorkspaceService workspaceService;
    private LanguageClient client;

    public PreproLanguageServer() {
        logger.log(Level.FINE, "<init>");
        this.textDocumentService = new PreproTextDocumentService();
        workspaceService = new PreproWorkspaceService();
    }


    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
        logger.log(Level.FINE, "Initialize(%s)", params);
        return CompletableFuture.supplyAsync(() -> {
            final ServerCapabilities serverCapabilities = new ServerCapabilities();
            serverCapabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
            serverCapabilities.setCompletionProvider(new CompletionOptions());

            final ServerInfo serverInfo = new ServerInfo(ServerInformation.name, ServerInformation.version);
            return new InitializeResult(serverCapabilities, serverInfo);
        });
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        logger.log(Level.FINE, "Shutdown");
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void exit() {
        logger.log(Level.FINE, "Exit");
    }

    @Nonnull
    @Override
    public TextDocumentService getTextDocumentService() {
        logger.log(Level.FINE, "getTextDocumentService");
        return textDocumentService;
    }

    @Nonnull
    @Override
    public WorkspaceService getWorkspaceService() {
        logger.log(Level.FINE, "getWorkspaceService");
        return workspaceService;
    }

    @Override
    public void initialized(InitializedParams params) {
        logger.log(Level.FINE, "Initialized(%s)", params);
        //Nothing to do yet
    }

    @Override
    public void cancelProgress(WorkDoneProgressCancelParams params) {
        logger.log(Level.FINE, "cancelProgress(%s)", params);
        throw new UnsupportedOperationException();
    }

    @Override
    public void connect(LanguageClient client) {
        logger.log(Level.FINE, "connect(%s)", client);
        this.client = client;
    }

    public LanguageClient getClient() {
        logger.log(Level.FINE, "client");
        return client;
    }
}
