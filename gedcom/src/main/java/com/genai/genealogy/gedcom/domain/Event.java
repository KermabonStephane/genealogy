package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Event {
    String type; // e.g., BIRT, DEAT, MARR
    String date;
    String place;
    String description;
}
