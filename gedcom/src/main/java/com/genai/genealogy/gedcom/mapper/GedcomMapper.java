package com.genai.genealogy.gedcom.mapper;

import com.genai.genealogy.gedcom.domain.Event;
import com.genai.genealogy.gedcom.domain.Family;
import com.genai.genealogy.gedcom.domain.GedcomVersion;
import com.genai.genealogy.gedcom.domain.Header;
import com.genai.genealogy.gedcom.domain.HeaderSource;
import com.genai.genealogy.gedcom.domain.Individual;
import com.genai.genealogy.gedcom.domain.Note;
import com.genai.genealogy.gedcom.domain.PersonalName;
import com.genai.genealogy.gedcom.domain.Source;
import com.genai.genealogy.gedcom.domain.Submitter;
import com.genai.genealogy.gedcom.parser.RawRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GedcomMapper {
    public static final GedcomMapper INSTANCE = new GedcomMapper();

    public Header toHeader(RawRecord raw) {
        Header.HeaderBuilder builder = Header.builder();
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "SOUR" -> builder.source(toHeaderSource(child));
                case "DATE" -> builder.date(toDateTime(child));
                case "SUBM" -> builder.submitter(toSubmitter(child));
                case "FILE" -> builder.file(child.value());
                case "COPR" -> builder.copyright(child.value());
                case "GEDC" -> builder.gedcomVersion(toGedcomVersion(child));
                case "CHAR" -> builder.encoding(child.value());
                case "LANG" -> builder.language(child.value());
            }
        });
        return builder.build();
    }

    private GedcomVersion toGedcomVersion(RawRecord raw) {
        GedcomVersion.GedcomVersionBuilder builder = GedcomVersion.builder();
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "VERS" -> builder.version(child.value());
            }
        });
        return builder.build();
    }

    private LocalDateTime toDateTime(RawRecord raw) {
//        LocalDateTime date = LocalDateTime.parse(raw.value(), DateTimeFormatter.ofPattern("dd MM yyyy"));
//        return date;
        return null;
    }

    private HeaderSource toHeaderSource(RawRecord raw) {
        HeaderSource.HeaderSourceBuilder builder = HeaderSource.builder();
        builder.name(raw.value());
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "VERS" -> builder.version(child.value());
            }
        });
        return builder.build();
    }

    public Individual toIndividual(RawRecord raw) {
        Individual.IndividualBuilder builder = Individual.builder();
        builder.id(raw.id());
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "NAME" -> builder.name(toPersonalName(child));
                case "SEX" -> builder.sex(child.value());
            }
        });
        return builder.build();
    }

    private PersonalName toPersonalName(RawRecord child) {
        PersonalName.PersonalNameBuilder builder = PersonalName.builder();
        builder.name(child.value());
        child.children().forEach(grandChild -> {
            switch (grandChild.tag()) {
                case "GIVN" -> builder.givenName(grandChild.value());
                case "SURN" -> builder.surname(grandChild.value());
                case "NICK" -> builder.nickName(grandChild.value());
                case "NPFX" -> builder.prefixName(grandChild.value());
                case "SPFX" -> builder.surnamePrefix(grandChild.value());
                case "NSFX" -> builder.suffixName(grandChild.value());
            };
        });
        return builder.build();
    }

    public Family toFamily(RawRecord raw) {
        Family.FamilyBuilder builder = Family.builder();
        List<String> husbandId = new ArrayList<>();
        List<String> wifeId = new ArrayList<>();
        List<String> childrenId = new ArrayList<>();
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "HUSB" -> husbandId.add(sanitizeId(child.value()));
                case "WIFE" -> wifeId.add(sanitizeId(child.value()));
                case "CHIL" -> childrenId.add(sanitizeId(child.value()));
            }
        });
        if (!husbandId.isEmpty()) {
            builder.husbandIds(husbandId);
        }
        if (!wifeId.isEmpty()) {
            builder.wifeIds(wifeId);
        }
        if (!childrenId.isEmpty()) {
            builder.childrenIds(childrenId);
        }
        return builder.id(raw.id()).build();
    }

    public Source toSource(RawRecord raw) {
        return Source.builder().build();
    }

    public Submitter toSubmitter(RawRecord raw) {
        return Submitter.builder().id(raw.value()).build();
    }

    public Event toEvent(RawRecord raw) {
        return Event.builder().build();
    }

    private String sanitizeId(String id) {
        if (id == null) return "";
        return id.replace("@", "");
    }

    public Note toNote(RawRecord raw) {
        Note.NoteBuilder builder = Note.builder();
        StringBuffer content = new StringBuffer();
        content.append(raw.value());
        raw.children().forEach(child -> {
            switch (child.tag()) {
                case "CONT" -> content.append(child.value()).append("\n");
            }
        });
        if (!content.isEmpty()) {
            builder.content(content.toString());
        }
        return builder.id(raw.id()).build();
    }
}
