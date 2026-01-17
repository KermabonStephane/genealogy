package com.genai.genealogy.gedcom.mapper;

import com.genai.genealogy.gedcom.GedcomIdentifierSanitizer;
import com.genai.genealogy.gedcom.domain.Event;
import com.genai.genealogy.gedcom.domain.Family;
import com.genai.genealogy.gedcom.domain.GedcomTag;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GedcomMapper implements GedcomIdentifierSanitizer {
    public static final GedcomMapper INSTANCE = new GedcomMapper();

    public Header toHeader(RawRecord raw) {
        Header.HeaderBuilder builder = Header.builder();
        raw.children().forEach(child -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case SOURCE -> builder.source(toHeaderSource(child));
                    case DATE -> builder.date(toDateTime(child));
                    case SUBMITTER -> builder.submitter(toSubmitter(child));
                    case FILE -> builder.file(child.value());
                    case COPYRIGHT -> builder.copyright(child.value());
                    case GEDCOM ->
                            builder.gedcomVersion(toGedcomVersion(child));
                    case CHARACTER_SET -> builder.encoding(child.value());
                    case LANGUAGE -> builder.language(child.value());
                }
            }
        });
        return builder.build();
    }

    private GedcomVersion toGedcomVersion(RawRecord raw) {
        GedcomVersion.GedcomVersionBuilder builder = GedcomVersion.builder();
        raw.children().forEach(child -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case VERSION -> builder.version(child.value());
                }
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
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case VERSION -> builder.version(child.value());
                }
            }
        });
        return builder.build();
    }

    public Individual toIndividual(RawRecord raw) {
        Individual.IndividualBuilder builder = Individual.builder();
        builder.id(raw.id());
        raw.children().forEach(child -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case NAME -> builder.name(toPersonalName(child));
                    case SEX -> builder.sex(child.value());
                }
            }
        });
        return builder.build();
    }

    private PersonalName toPersonalName(RawRecord child) {
        PersonalName.PersonalNameBuilder builder = PersonalName.builder();
        builder.name(child.value());
        child.children().forEach(grandChild -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(grandChild.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case GIVEN_NAME -> builder.givenName(grandChild.value());
                    case SURNAME -> builder.surname(grandChild.value());
                    case NICKNAME -> builder.nickName(grandChild.value());
                    case NAME_PREFIX -> builder.prefixName(grandChild.value());
                    case SURNAME_PREFIX ->
                            builder.surnamePrefix(grandChild.value());
                    case NAME_SUFFIX -> builder.suffixName(grandChild.value());
                }
            }
        });
        return builder.build();
    }

    public Family toFamily(RawRecord raw) {
        Family.FamilyBuilder builder = Family.builder();
        List<String> husbandId = new ArrayList<>();
        List<String> wifeId = new ArrayList<>();
        List<String> childrenId = new ArrayList<>();
        raw.children().forEach(child -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case HUSBAND -> husbandId.add(sanitizeId(child.value()));
                    case WIFE -> wifeId.add(sanitizeId(child.value()));
                    case CHILD -> childrenId.add(sanitizeId(child.value()));
                }
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

    public Note toNote(RawRecord raw) {
        Note.NoteBuilder builder = Note.builder();
        StringBuffer content = new StringBuffer();
        content.append(raw.value());
        raw.children().forEach(child -> {
            Optional<GedcomTag> tag = GedcomTag.fromTag(child.tag());
            if (tag.isPresent()) {
                switch (tag.get()) {
                    case CONTINUED ->
                            content.append(child.value()).append("\n");
                }
            }
        });
        if (!content.isEmpty()) {
            builder.content(content.toString());
        }
        return builder.id(raw.id()).build();
    }
}
