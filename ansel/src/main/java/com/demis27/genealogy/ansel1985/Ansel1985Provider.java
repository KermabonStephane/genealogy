package com.demis27.genealogy.ansel1985;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Collections;
import java.util.Iterator;

public class Ansel1985Provider extends CharsetProvider {

    private static final String CHARSET_NAME = "ANSEL";
    private Charset charset;

    public Ansel1985Provider() {
        this.charset = new Ansel1985Charset(CHARSET_NAME, new String[0]);
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
