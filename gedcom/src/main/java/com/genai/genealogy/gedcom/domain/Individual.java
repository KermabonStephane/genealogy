package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Individual {
    String id;
    String name;
    String sex;
    List<Event> events;
    List<String> familyAsChildIds;
    List<String> familyAsSpouseIds;
    List<String> notes;
}
