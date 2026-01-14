package com.genai.genealogy.gedcom.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GedcomParser {

    private static final Pattern LINE_PATTERN = Pattern.compile("^(\\d+)\\s+(?:@([^@]+)@\\s+)?([^\\s]+)(?:\\s+(.*))?$");

    public List<RawRecord> parse(InputStream inputStream) throws IOException {
        List<RawRecord> topLevelRecords = new ArrayList<>();
        Stack<RawRecord> stack = new Stack<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                Matcher matcher = LINE_PATTERN.matcher(line);
                if (!matcher.matches()) continue;

                int level = Integer.parseInt(matcher.group(1));
                String id = matcher.group(2);
                String tag = matcher.group(3);
                String value = matcher.group(4);

                // Note: Sometimes the ID and Tag are swapped in GEDCOM for top-level records
                // e.g. "0 @I1@ INDI" -> level=0, id=I1, tag=INDI
                // But sometimes it's "0 HEAD" -> level=0, id=null, tag=HEAD
                // My regex handles both.

                RawRecord record = RawRecord.builder()
                        .level(level)
                        .id(id)
                        .tag(tag)
                        .value(value)
                        .build();

                if (level == 0) {
                    topLevelRecords.add(record);
                    stack.clear();
                    stack.push(record);
                } else {
                    while (!stack.isEmpty() && stack.peek().getLevel() >= level) {
                        stack.pop();
                    }
                    if (!stack.isEmpty()) {
                        stack.peek().getChildren().add(record);
                        stack.push(record);
                    }
                }
            }
        }
        return topLevelRecords;
    }
}
