package com.genai.genealogy.gedcom.domain;

import lombok.Builder;
import lombok.Data;

@Builder
public record HeaderSource (String id, String name, String version) {
}
