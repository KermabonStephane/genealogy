package com.demis27.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record HeaderSource (String id, String name, String version) {
}
