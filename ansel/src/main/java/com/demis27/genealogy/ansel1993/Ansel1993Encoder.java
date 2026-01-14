package com.demis27.genealogy.ansel1993;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.HashMap;
import java.util.Map;

public class Ansel1993Encoder extends CharsetEncoder {

    private static final Map<Character, Integer> UNICODE_TO_ANSEL = new HashMap<>();

    static {
        // ASCII characters
        for (int i = 0x20; i <= 0x7E; i++) {
            UNICODE_TO_ANSEL.put((char) i, i);
        }
        UNICODE_TO_ANSEL.put('\u0141', 0xA1); // Ł
        UNICODE_TO_ANSEL.put('\u00D8', 0xA2); // Ø
        UNICODE_TO_ANSEL.put('\u0110', 0xA3); // Đ
        UNICODE_TO_ANSEL.put('\u00DE', 0xA4); // Þ
        UNICODE_TO_ANSEL.put('\u00C6', 0xA5); // Æ
        UNICODE_TO_ANSEL.put('\u0152', 0xA6); // Œ
        UNICODE_TO_ANSEL.put('\u00B1', 0xA7); // ±
        UNICODE_TO_ANSEL.put('\u0131', 0xA8); // ı
        UNICODE_TO_ANSEL.put('\u0142', 0xA9); // ł
        UNICODE_TO_ANSEL.put('\u00F8', 0xAA); // ø
        UNICODE_TO_ANSEL.put('\u0111', 0xAB); // đ
        UNICODE_TO_ANSEL.put('\u00FE', 0xAC); // þ
        UNICODE_TO_ANSEL.put('\u00E6', 0xAD); // æ
        UNICODE_TO_ANSEL.put('\u0153', 0xAE); // œ
        UNICODE_TO_ANSEL.put('\u00DF', 0xAF); // ß
        UNICODE_TO_ANSEL.put('\u00A3', 0xB0); // £
        UNICODE_TO_ANSEL.put('\u00D0', 0xB1); // Ð
        UNICODE_TO_ANSEL.put('\u00B0', 0xB2); // °
        UNICODE_TO_ANSEL.put('\u00C7', 0xB3); // Ç
        UNICODE_TO_ANSEL.put('\u02BC', 0xBA); // MODIFIER LETTER APOSTROPHE
        UNICODE_TO_ANSEL.put('\u02BD', 0xBB); // MODIFIER LETTER REVERSED COMMA
        UNICODE_TO_ANSEL.put('\u02BE', 0xBC); // MODIFIER LETTER RIGHT HALF RING
        UNICODE_TO_ANSEL.put('\u02BF', 0xBD); // MODIFIER LETTER LEFT HALF RING
        UNICODE_TO_ANSEL.put('\u02C0', 0xBE); // MODIFIER LETTER GLOTTAL STOP
        UNICODE_TO_ANSEL.put('\u02C1', 0xBF); // MODIFIER LETTER REVERSED GLOTTAL STOP
    }

    public Ansel1993Encoder(Charset cs) {
        super(cs, 1.0f, 1.0f);
    }

    @Override
    protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
        while (in.hasRemaining()) {
            if (!out.hasRemaining()) {
                return CoderResult.OVERFLOW;
            }
            char c = in.get();
            Integer anselValue = UNICODE_TO_ANSEL.get(c);
            if (anselValue != null) {
                out.put(anselValue.byteValue());
            } else {
                return CoderResult.unmappableForLength(1);
            }
        }
        return CoderResult.UNDERFLOW;
    }
}
