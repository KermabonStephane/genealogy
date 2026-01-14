package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Source {
    String id;
    String title;
    String abbreviation;
    String publication;
    String text;
}
