package com.genai.genealogy.gedcom;

import com.genai.genealogy.gedcom.domain.Event;
import com.genai.genealogy.gedcom.domain.Family;
import com.genai.genealogy.gedcom.domain.Gedcom;
import com.genai.genealogy.gedcom.domain.Header;
import com.genai.genealogy.gedcom.domain.HeaderSource;
import com.genai.genealogy.gedcom.domain.Individual;
import com.genai.genealogy.gedcom.domain.Source;
import com.genai.genealogy.gedcom.domain.Submitter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GedcomWriter {

    public void write(Gedcom gedcom, OutputStream outputStream) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            writeHeader(writer, gedcom.header());

            if (gedcom.submitters() != null) {
                for (Submitter submitter : gedcom.submitters().values()) {
                    writeSubmitter(writer, submitter);
                }
            }

            if (gedcom.individuals() != null) {
                for (Individual individual : gedcom.individuals().values()) {
                    writeIndividual(writer, individual);
                }
            }

            if (gedcom.families() != null) {
                for (Family family : gedcom.families().values()) {
                    writeFamily(writer, family);
                }
            }

            if (gedcom.sources() != null) {
                for (Source source : gedcom.sources().values()) {
                    writeSource(writer, source);
                }
            }

            writeLine(writer, 0, null, "TRLR", null);
            writer.flush();
        }
    }

    private void writeHeader(BufferedWriter writer, Header header) throws IOException {
        if (header == null) return;
        writeLine(writer, 0, null, "HEAD", null);
        HeaderSource headerSource = header.source();
        if (headerSource != null) {
            writeHeaderSource(writer, headerSource);
        }
//        writeLine(writer, 1, null, "GEDC", null);
//        writeLine(writer, 2, null, "VERS", "5.5");
//        writeLine(writer, 2, null, "FORM", "LINEAGE-LINKED");
        writeLine(writer, 1, null, "CHAR", "UTF-8");
//        if (header.date() != null) {
//            writeLine(writer, 1, null, "DATE", header.date());
//            if (header.time() != null) {
//                writeLine(writer, 2, null, "TIME", header.time());
//            }
//        }
//        if (header.submitterId() != null) {
//            writeLine(writer, 1, null, "SUBM", "@" + sanitizeId(header.submitterId()) + "@");
//        }
    }

    private void writeHeaderSource(BufferedWriter writer, HeaderSource source) throws IOException {
        writeLine(writer, 1, null, "SOUR", source.name());
        if (source.version() != null)
            writeLine(writer, 2, null, "VERS", source.version());
    }

    private void writeIndividual(BufferedWriter writer, Individual indi) throws IOException {
        writeLine(writer, 0, indi.id(), "INDI", null);
        if (indi.name() != null)
            writeLine(writer, 1, null, "NAME", indi.name().name());
        if (indi.sex() != null) writeLine(writer, 1, null, "SEX", indi.sex());

        writeEvents(writer, indi.events(), 1);

        if (indi.familyAsChildIds() != null) {
            for (String famId : indi.familyAsChildIds()) {
                writeLine(writer, 1, null, "FAMC", "@" + sanitizeId(famId) + "@");
            }
        }
        if (indi.familyAsSpouseIds() != null) {
            for (String famId : indi.familyAsSpouseIds()) {
                writeLine(writer, 1, null, "FAMS", "@" + sanitizeId(famId) + "@");
            }
        }
        if (indi.notes() != null) {
            for (String note : indi.notes()) {
                writeLine(writer, 1, null, "NOTE", note);
            }
        }
    }

    private void writeFamily(BufferedWriter writer, Family fam) throws IOException {
        writeLine(writer, 0, fam.id(), "FAM", null);
        if (fam.husbandIds() != null) {
            for (String husbandId : fam.husbandIds()) {
                writeLine(writer, 1, null, "HUSB", "@" + sanitizeId(husbandId) + "@");
            }
        }
        if (fam.wifeIds() != null) {
            for (String wifeId : fam.wifeIds()) {
                writeLine(writer, 1, null, "WIFE", "@" + sanitizeId(wifeId) + "@");
            }
        }
        if (fam.childrenIds() != null) {
            for (String childId : fam.childrenIds()) {
                writeLine(writer, 1, null, "CHIL", "@" + sanitizeId(childId) + "@");
            }
        }

        writeEvents(writer, fam.events(), 1);

        if (fam.notes() != null) {
            for (String note : fam.notes()) {
                writeLine(writer, 1, null, "NOTE", note);
            }
        }
    }

    private void writeSource(BufferedWriter writer, Source source) throws IOException {
        writeLine(writer, 0, source.id(), "SOUR", null);
        if (source.title() != null)
            writeLine(writer, 1, null, "TITL", source.title());
        if (source.abbreviation() != null)
            writeLine(writer, 1, null, "ABBR", source.abbreviation());
        if (source.publication() != null)
            writeLine(writer, 1, null, "PUBL", source.publication());
        if (source.text() != null)
            writeLine(writer, 1, null, "TEXT", source.text());
    }

    private void writeSubmitter(BufferedWriter writer, Submitter subm) throws IOException {
        writeLine(writer, 0, subm.id(), "SUBM", null);
        if (subm.name() != null)
            writeLine(writer, 1, null, "NAME", subm.name());
        if (subm.address() != null)
            writeLine(writer, 1, null, "ADDR", subm.address());
    }

    private void writeEvents(BufferedWriter writer, List<Event> events, int level) throws IOException {
        if (events == null) return;
        for (Event event : events) {
            writeLine(writer, level, null, event.type(), event.description());
            if (event.date() != null)
                writeLine(writer, level + 1, null, "DATE", event.date());
            if (event.place() != null)
                writeLine(writer, level + 1, null, "PLAC", event.place());
        }
    }

    private void writeLine(BufferedWriter writer, int level, String id, String tag, String value) throws IOException {
        writer.write(String.valueOf(level));
        if (id != null) {
            writer.write(" @");
            writer.write(sanitizeId(id));
            writer.write("@");
        }
        writer.write(" ");
        writer.write(tag);
        if (value != null && !value.isEmpty()) {
            writer.write(" ");
            writer.write(value);
        }
        writer.write("\n");
    }

    private String sanitizeId(String id) {
        if (id == null) return "";
        return id.replace("@", "");
    }
}
