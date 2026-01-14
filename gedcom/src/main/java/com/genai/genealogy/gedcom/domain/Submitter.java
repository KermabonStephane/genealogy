package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Submitter(
    String id,
    String name,
    String address
) {}