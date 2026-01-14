package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Header(
    String source,
    String version,
    String gedcomVersion,
    String encoding,
    String date,
    String time,
    String submitterId
) {}