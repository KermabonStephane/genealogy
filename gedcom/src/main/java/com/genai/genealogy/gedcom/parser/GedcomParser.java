package com.genai.genealogy.gedcom.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GedcomParser {

    private static final Pattern LINE_PATTERN = Pattern.compile("^(\\d+)\\s+(?:@([^@]+)@\\s+)?([^\\s]+)(?:\\s+(.*))?$");

    public List<RawRecord> parse(InputStream inputStream, java.nio.charset.Charset charset) throws IOException {
        List<RawRecord> topLevelRecords = new ArrayList<>();
        Stack<RawRecord> stack = new Stack<>();

        byte[] bytes = inputStream.readAllBytes();
        List<String> lines = splitLines(bytes, charset);

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher matcher = LINE_PATTERN.matcher(line);
            if (!matcher.matches()) continue;

            int level = Integer.parseInt(matcher.group(1));
            String id = matcher.group(2);
            String tag = matcher.group(3);
            String value = matcher.group(4);

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
                while (!stack.isEmpty() && stack.peek().level() >= level) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    stack.peek().children().add(record);
                    stack.push(record);
                }
            }
        }
        return topLevelRecords;
    }

    private List<String> splitLines(byte[] bytes, java.nio.charset.Charset charset) {
        List<String> lines = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 0x0A || bytes[i] == 0x0D) {
                if (i > start) {
                    lines.add(new String(bytes, start, i - start, charset));
                }
                if (bytes[i] == 0x0D && i + 1 < bytes.length && bytes[i+1] == 0x0A) {
                    i++;
                }
                start = i + 1;
            }
        }
        if (start < bytes.length) {
            lines.add(new String(bytes, start, bytes.length - start, charset));
        }
        return lines;
    }
}
