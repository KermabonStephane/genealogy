package com.genai.genealogy.gedcom.parser;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Builder
public class RawRecord {
    private int level;
    private String id;
    private String tag;
    private String value;
    @Builder.Default
    private List<RawRecord> children = new ArrayList<>();

    public Optional<RawRecord> getChild(String tag) {
        return children.stream()
                .filter(c -> c.getTag().equals(tag))
                .findFirst();
    }

    public List<RawRecord> getChildren(String tag) {
        return children.stream()
                .filter(c -> c.getTag().equals(tag))
                .toList();
    }

    public String getChildValue(String tag) {
        return getChild(tag).map(RawRecord::getValue).orElse(null);
    }
}
