package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class Gedcom {
    Header header;
    Map<String, Individual> individuals;
    Map<String, Family> families;
    Map<String, Source> sources;
    Map<String, Submitter> submitters;
}
