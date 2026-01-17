package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record Family(
    String id,
    List<String> husbandIds,
    List<String> wifeIds,
    List<String> childrenIds,
    List<Event> events,
    List<String> notes
) {}