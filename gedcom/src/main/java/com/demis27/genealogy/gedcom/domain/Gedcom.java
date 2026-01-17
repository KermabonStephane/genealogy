package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

import java.util.Map;

@Builder
public record Gedcom(
    Header header,
    Map<String, Individual> individuals,
    Map<String, Family> families,
    Map<String, Source> sources,
    Map<String, Submitter> submitters,
    Map<String, Note> notes
) {}