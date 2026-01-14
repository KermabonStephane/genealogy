package com.demis27.genealogy.ansel1993;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class Ansel1993Charset extends Charset {

    public Ansel1993Charset(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
    }

    @Override
    public boolean contains(Charset cs) {
        return false;
    }

    @Override
    public CharsetDecoder newDecoder() {
        return new Ansel1993Decoder(this);
    }

    @Override
    public CharsetEncoder newEncoder() {
        return new Ansel1993Encoder(this);
    }
}
