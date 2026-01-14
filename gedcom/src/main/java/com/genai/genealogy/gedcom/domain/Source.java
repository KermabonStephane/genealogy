package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Source(
    String id,
    String title,
    String abbreviation,
    String publication,
    String text
) {}