package com.genai.genealogy.gedcom;

import com.genai.genealogy.gedcom.domain.Gedcom;
import com.genai.genealogy.gedcom.mapper.GedcomMapper;
import com.genai.genealogy.gedcom.parser.GedcomParser;
import com.genai.genealogy.gedcom.parser.RawRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GedcomReader {
    private final GedcomParser parser = new GedcomParser();
    private final GedcomMapper mapper = GedcomMapper.INSTANCE;

    public Gedcom read(InputStream inputStream) throws IOException {
        List<RawRecord> rawRecords = parser.parse(inputStream);
        return mapper.toGedcom(rawRecords);
    }
}
