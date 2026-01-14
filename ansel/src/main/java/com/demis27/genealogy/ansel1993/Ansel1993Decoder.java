package com.demis27.genealogy.ansel1993;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.HashMap;
import java.util.Map;

public class Ansel1993Decoder extends CharsetDecoder {

    private static final Map<Integer, Character> ANSEL_TO_UNICODE = new HashMap<>();

    static {
        // ASCII characters
        for (int i = 0x20; i <= 0x7E; i++) {
            ANSEL_TO_UNICODE.put(i, (char) i);
        }
        ANSEL_TO_UNICODE.put(0xA1, '\u0141'); // Ł
        ANSEL_TO_UNICODE.put(0xA2, '\u00D8'); // Ø
        ANSEL_TO_UNICODE.put(0xA3, '\u0110'); // Đ
        ANSEL_TO_UNICODE.put(0xA4, '\u00DE'); // Þ
        ANSEL_TO_UNICODE.put(0xA5, '\u00C6'); // Æ
        ANSEL_TO_UNICODE.put(0xA6, '\u0152'); // Œ
        ANSEL_TO_UNICODE.put(0xA7, '\u00B1'); // ±
        ANSEL_TO_UNICODE.put(0xA8, '\u0131'); // ı
        ANSEL_TO_UNICODE.put(0xA9, '\u0142'); // ł
        ANSEL_TO_UNICODE.put(0xAA, '\u00F8'); // ø
        ANSEL_TO_UNICODE.put(0xAB, '\u0111'); // đ
        ANSEL_TO_UNICODE.put(0xAC, '\u00FE'); // þ
        ANSEL_TO_UNICODE.put(0xAD, '\u00E6'); // æ
        ANSEL_TO_UNICODE.put(0xAE, '\u0153'); // œ
        ANSEL_TO_UNICODE.put(0xAF, '\u00DF'); // ß
        ANSEL_TO_UNICODE.put(0xB0, '\u00A3'); // £
        ANSEL_TO_UNICODE.put(0xB1, '\u00D0'); // Ð
        ANSEL_TO_UNICODE.put(0xB2, '\u00B0'); // °
        ANSEL_TO_UNICODE.put(0xB3, '\u00C7'); // Ç
        ANSEL_TO_UNICODE.put(0xBA, '\u02BC'); // MODIFIER LETTER APOSTROPHE
        ANSEL_TO_UNICODE.put(0xBB, '\u02BD'); // MODIFIER LETTER REVERSED COMMA
        ANSEL_TO_UNICODE.put(0xBC, '\u02BE'); // MODIFIER LETTER RIGHT HALF RING
        ANSEL_TO_UNICODE.put(0xBD, '\u02BF'); // MODIFIER LETTER LEFT HALF RING
        ANSEL_TO_UNICODE.put(0xBE, '\u02C0'); // MODIFIER LETTER GLOTTAL STOP
        ANSEL_TO_UNICODE.put(0xBF, '\u02C1'); // MODIFIER LETTER REVERSED GLOTTAL STOP
    }

    public Ansel1993Decoder(Charset cs) {
        super(cs, 1.0f, 1.0f);
    }

    @Override
    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
        while (in.hasRemaining()) {
            if (!out.hasRemaining()) {
                return CoderResult.OVERFLOW;
            }
            int b = in.get() & 0xFF;
            Character c = ANSEL_TO_UNICODE.get(b);
            if (c != null) {
                out.put(c);
            } else {
                // Handle unmappable characters
                out.put('?');
            }
        }
        return CoderResult.UNDERFLOW;
    }
}
