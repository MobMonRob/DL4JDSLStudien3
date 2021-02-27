package de.dhbw.ka.prepro.lsp.server.services;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PreproTextDocumentService implements TextDocumentService {

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams parameter) {
        return CompletableFuture.supplyAsync(() -> {
            final List<CompletionItem> list = new ArrayList<>();

            final CompletionItem mainSnippet = new CompletionItem("main");
            mainSnippet.setKind(CompletionItemKind.Snippet);
            mainSnippet.setInsertText("function main() {\n\n}");
            mainSnippet.setDetail("Creates that super main function");
            list.add(mainSnippet);

            return Either.forRight(new CompletionList(list));
        });
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {

    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {

    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {

    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {

    }
}
