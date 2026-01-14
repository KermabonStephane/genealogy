package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Header {
    String source;
    String version;
    String gedcomVersion;
    String encoding;
    String date;
    String time;
    String submitterId;
}
