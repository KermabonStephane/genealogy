package com.demis27.genealogy.ansel1993;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.Iterator;

public class Ansel1993Provider extends CharsetProvider {

    private static final String CHARSET_NAME = "ANSEL";
    private Charset charset;

    public Ansel1993Provider() {
        this.charset = new Ansel1993Charset(CHARSET_NAME, new String[0]);
    }

    @Override
    public Iterator<Charset> charsets() {
        return Collections.singleton(charset).iterator();
    }

    @Override
    public Charset charsetForName(String charsetName) {
        if (charsetName.equalsIgnoreCase(CHARSET_NAME)) {
            return charset;
        }
        return null;
    }
}

