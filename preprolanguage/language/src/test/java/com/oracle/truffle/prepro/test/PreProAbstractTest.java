package com.oracle.truffle.prepro.test;

import com.oracle.truffle.prepro.PreProPolyglotContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class PreProAbstractTest {

    PreProPolyglotContext context;
    private OutputStream os;

    @BeforeEach
    void setUp() {
        os = new ByteArrayOutputStream();
        context = new PreProPolyglotContext(os);
    }

    @AfterEach
    void tearDown() {
        context.cleanup();
    }

    /**
     * Converts a {@link ByteArrayOutputStream} content into UTF-8 String with UNIX line ends.
     */
    String toUnixString() {
        return ((ByteArrayOutputStream) os).toString(UTF_8).replace("\r\n", "\n");
    }
}
