package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Family {
    String id;
    String husbandId;
    String wifeId;
    List<String> childrenIds;
    List<Event> events;
    List<String> notes;
}
