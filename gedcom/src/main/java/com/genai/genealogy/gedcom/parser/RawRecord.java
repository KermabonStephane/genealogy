package com.genai.genealogy.gedcom.parser;

import lombok.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Builder
public record RawRecord(
    int level,
    String id,
    String tag,
    String value,
    List<RawRecord> children
) {
    public RawRecord {
        if (children == null) {
            children = new ArrayList<>();
        }
    }

    public Optional<RawRecord> getChild(String tag) {
        return children.stream()
                .filter(c -> c.tag().equals(tag))
                .findFirst();
    }

    public List<RawRecord> getChildren(String tag) {
        return children.stream()
                .filter(c -> c.tag().equals(tag))
                .toList();
    }

    public String getChildValue(String tag) {
        return getChild(tag).map(RawRecord::value).orElse(null);
    }
}