package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Submitter(
    String id,
    String name,
    String address
) {}