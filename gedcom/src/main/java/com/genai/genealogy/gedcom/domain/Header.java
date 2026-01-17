package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Header(
    HeaderSource source,
    GedcomVersion gedcomVersion,
    String encoding,
    LocalDateTime date,
    Submitter submitter,
    String file,
    String copyright,
    String language
) {}