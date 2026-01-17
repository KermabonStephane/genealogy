package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record Note(String id, String content) {
}
