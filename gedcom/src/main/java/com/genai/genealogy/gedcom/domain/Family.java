package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import java.util.List;

@Builder
public record Family(
    String id,
    String husbandId,
    String wifeId,
    List<String> childrenIds,
    List<Event> events,
    List<String> notes
) {}