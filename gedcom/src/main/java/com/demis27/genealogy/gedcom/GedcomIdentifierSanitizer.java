package com.demis27.genealogy.gedcom;

public interface GedcomIdentifierSanitizer {

    default String sanitizeId(String id) {
        if (id == null) return "";
        return id.replace("@", "");
    }
}
