package com.demis27.genealogy.gedcom;

import com.demis27.genealogy.gedcom.domain.Family;
import com.demis27.genealogy.gedcom.domain.Gedcom;
import com.demis27.genealogy.gedcom.domain.GedcomTag;
import com.demis27.genealogy.gedcom.domain.Header;
import com.demis27.genealogy.gedcom.domain.Individual;
import com.demis27.genealogy.gedcom.domain.Note;
import com.demis27.genealogy.gedcom.domain.Source;
import com.demis27.genealogy.gedcom.domain.Submitter;
import com.demis27.genealogy.gedcom.mapper.GedcomMapper;
import com.demis27.genealogy.gedcom.parser.GedcomParser;
import com.demis27.genealogy.gedcom.parser.RawRecord;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GedcomReader {
    private final GedcomParser parser = new GedcomParser();
    private final GedcomMapper mapper = GedcomMapper.INSTANCE;

    public Gedcom read(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }

        Charset charset = detectCharset(inputStream);

        List<RawRecord> rawRecords = parser.parse(inputStream, charset);
        
        Header header = null;
        Map<String, Individual> individuals = new HashMap<>();
        Map<String, Family> families = new HashMap<>();
        Map<String, Source> sources = new HashMap<>();
        Map<String, Submitter> submitters = new HashMap<>();
        Map<String, Note> notes = new HashMap<>();

        for (RawRecord raw : rawRecords) {
            if (GedcomTag.HEAD.getTag().equals(raw.tag())) {
                header = mapper.toHeader(raw);
            } else if (GedcomTag.INDIVIDUAL.getTag().equals(raw.tag())) {
                individuals.put(raw.id(), mapper.toIndividual(raw));
            } else if (GedcomTag.FAMILY.getTag().equals(raw.tag())) {
                families.put(raw.id(), mapper.toFamily(raw));
            } else if (GedcomTag.SOURCE.getTag().equals(raw.tag())) {
                sources.put(raw.id(), mapper.toSource(raw));
            } else if (GedcomTag.SUBMITTER.getTag().equals(raw.tag())) {
                submitters.put(raw.id(), mapper.toSubmitter(raw));
            } else if (GedcomTag.NOTE.getTag().equals(raw.tag())) {
                notes.put(raw.id(), mapper.toNote(raw));
            }
        }

        return Gedcom.builder()
                .header(header)
                .individuals(individuals)
                .families(families)
                .sources(sources)
                .submitters(submitters)
                .notes(notes)
                .build();
    }

    private Charset detectCharset(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        is.mark(1024);
        int read = is.read(buffer);
        is.reset();
        if (read <= 0) return StandardCharsets.UTF_8;

        String content = new String(buffer, 0, read, StandardCharsets.ISO_8859_1);
        String charsetName = "UTF-8";

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("1\\s+" + GedcomTag.CHARACTER_SET.getTag() + "\\s+([^\\r\\n]+)");
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
