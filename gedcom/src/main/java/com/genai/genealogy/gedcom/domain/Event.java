package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Event(
    String type,
    String date,
    String place,
    String description
) {}