package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder(toBuilder = true)
public record GedcomVersion(String version) {
}
