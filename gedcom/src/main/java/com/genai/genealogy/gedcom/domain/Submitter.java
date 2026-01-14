package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Submitter {
    String id;
    String name;
    String address;
}
