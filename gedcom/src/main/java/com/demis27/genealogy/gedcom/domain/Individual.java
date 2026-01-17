package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

import java.util.List;

@Builder
public record Individual(
    String id,
    PersonalName name,
    String sex,
    List<Event> events,
    List<String> familyAsChildIds,
    List<String> familyAsSpouseIds,
    List<String> notes
) {}