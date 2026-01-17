package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Note(String id, String content) {
}
