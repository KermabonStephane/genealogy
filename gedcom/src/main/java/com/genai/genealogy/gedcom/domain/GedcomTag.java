package com.genai.genealogy.gedcom.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum GedcomTag {
    SOURCE("SOUR", "Source"),
    DATE("DATE", "Date"),
    SUBMITTER("SUBM", "Submitter"),
    FILE("FILE", "File"),
    COPYRIGHT("COPR", "Copyright"),
    GEDCOM("GEDC", "GEDCOM"),
    CHARACTER_SET("CHAR", "Character Set"),
    LANGUAGE("LANG", "Language"),
    VERSION("VERS", "Version"),
    NAME("NAME", "Name"),
    SEX("SEX", "Sex"),
    GIVEN_NAME("GIVN", "Given Name"),
    SURNAME("SURN", "Surname"),
    NICKNAME("NICK", "Nickname"),
    NAME_PREFIX("NPFX", "Name Prefix"),
    SURNAME_PREFIX("SPFX", "Surname Prefix"),
    NAME_SUFFIX("NSFX", "Name Suffix"),
    HUSBAND("HUSB", "Husband"),
    WIFE("WIFE", "Wife"),
    CHILD("CHIL", "Child"),
    CONTINUED("CONT", "Continued"),
    INDIVIDUAL("INDI", "Individual"),
    FAMILY("FAM", "Family"),
    NOTE("NOTE", "Note"),
    HEAD("HEAD", "Header"),
    TRAILER("TRLR", "Trailer"),
    FAMILY_CHILD("FAMC", "Family Child"),
    FAMILY_SPOUSE("FAMS", "Family Spouse"),
    TITLE("TITL", "Title"),
    ABBREVIATION("ABBR", "Abbreviation"),
    PUBLICATION("PUBL", "Publication"),
    TEXT("TEXT", "Text"),
    ADDRESS("ADDR", "Address"),
    PLACE("PLAC", "Place"),
    BIRTH("BIRT", "Birth"),
    DEATH("DEAT", "Death"),
    MARRIAGE("MARR", "Marriage"),
    TIME("TIME", "Time"),
    FORM("FORM", "Form");

    private final String tag;
    private final String definition;

    public static Optional<GedcomTag> fromTag(String tag) {
        return Arrays.stream(values())
                .filter(gedcomTag -> gedcomTag.tag.equals(tag))
                .findFirst();
    }
}
