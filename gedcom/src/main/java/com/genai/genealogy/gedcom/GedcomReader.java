package com.genai.genealogy.gedcom;

import com.genai.genealogy.gedcom.domain.Gedcom;
import com.genai.genealogy.gedcom.mapper.GedcomMapper;
import com.genai.genealogy.gedcom.parser.GedcomParser;
import com.genai.genealogy.gedcom.parser.RawRecord;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GedcomReader {
    private final GedcomParser parser = new GedcomParser();
    private final GedcomMapper mapper = GedcomMapper.INSTANCE;

    public Gedcom read(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }

        Charset charset = detectCharset(inputStream);

        List<RawRecord> rawRecords = parser.parse(inputStream, charset);
        return mapper.toGedcom(rawRecords);
    }

    private Charset detectCharset(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        is.mark(1024);
        int read = is.read(buffer);
        is.reset();
        if (read <= 0) return StandardCharsets.UTF_8;

        String content = new String(buffer, 0, read, StandardCharsets.ISO_8859_1);
        String charsetName = "UTF-8";
        
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("1\\s+CHAR\\s+([^\\r\\n]+)");
        java.util.regex.Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            charsetName = matcher.group(1).trim();
        }
        
        try {
            return switch (charsetName.toUpperCase()) {
                case "ANSEL" -> getAnselCharset();
                case "UNICODE" -> StandardCharsets.UTF_16;
                case "ASCII" -> StandardCharsets.US_ASCII;
                case "IBMPC", "IBM PC" -> Charset.forName("Cp437");
                default -> Charset.forName(charsetName);
            };
        } catch (Exception e) {
            return StandardCharsets.UTF_8;
        }
    }

    private Charset getAnselCharset() {
        try {
            return Charset.forName("ANSEL");
        } catch (Exception e) {
            try {
                return Charset.forName("Ansel1993");
            } catch (Exception e2) {
                try {
                    return Charset.forName("Ansel1985");
                } catch (Exception e3) {
                    return StandardCharsets.ISO_8859_1; // Fallback to something that handles 8-bit
                }
            }
        }
    }
}
