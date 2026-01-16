package com.genai.genealogy.gedcom.mapper;

import com.genai.genealogy.gedcom.domain.Event;
import com.genai.genealogy.gedcom.domain.Family;
import com.genai.genealogy.gedcom.domain.Gedcom;
import com.genai.genealogy.gedcom.domain.Header;
import com.genai.genealogy.gedcom.domain.Individual;
import com.genai.genealogy.gedcom.domain.Source;
import com.genai.genealogy.gedcom.domain.Submitter;
import com.genai.genealogy.gedcom.parser.RawRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface GedcomMapper {
    GedcomMapper INSTANCE = Mappers.getMapper(GedcomMapper.class);


    @Mapping(target = "source", expression = "java(raw.getChildValue(\"SOUR\"))")
    @Mapping(target = "version", expression = "java(raw.getChild(\"SOUR\").map(s -> s.getChildValue(\"VERS\")).orElse(null))")
    @Mapping(target = "gedcomVersion", expression = "java(raw.getChild(\"GEDC\").map(g -> g.getChildValue(\"VERS\")).orElse(null))")
    @Mapping(target = "encoding", expression = "java(raw.getChildValue(\"CHAR\"))")
    @Mapping(target = "date", expression = "java(raw.getChildValue(\"DATE\"))")
    @Mapping(target = "time", expression = "java(raw.getChildValue(\"TIME\"))")
    @Mapping(target = "submitterId", expression = "java(raw.getChildValue(\"SUBM\"))")
    Header toHeader(RawRecord raw);

    @Mapping(target = "name", expression = "java(raw.getChildValue(\"NAME\"))")
    @Mapping(target = "sex", expression = "java(raw.getChildValue(\"SEX\"))")
    @Mapping(target = "events", expression = "java(mapEvents(raw))")
    @Mapping(target = "familyAsChildIds", expression = "java(mapTags(raw, \"FAMC\"))")
    @Mapping(target = "familyAsSpouseIds", expression = "java(mapTags(raw, \"FAMS\"))")
    @Mapping(target = "notes", expression = "java(mapTags(raw, \"NOTE\"))")
    Individual toIndividual(RawRecord raw);

    @Mapping(target = "husbandId", expression = "java(raw.getChildValue(\"HUSB\"))")
    @Mapping(target = "wifeId", expression = "java(raw.getChildValue(\"WIFE\"))")
    @Mapping(target = "childrenIds", expression = "java(mapTags(raw, \"CHIL\"))")
    @Mapping(target = "events", expression = "java(mapEvents(raw))")
    @Mapping(target = "notes", expression = "java(mapTags(raw, \"NOTE\"))")
    Family toFamily(RawRecord raw);

    @Mapping(target = "title", expression = "java(raw.getChildValue(\"TITL\"))")
    @Mapping(target = "abbreviation", expression = "java(raw.getChildValue(\"ABBR\"))")
    @Mapping(target = "publication", expression = "java(raw.getChildValue(\"PUBL\"))")
    @Mapping(target = "text", expression = "java(raw.getChildValue(\"TEXT\"))")
    Source toSource(RawRecord raw);

    @Mapping(target = "name", expression = "java(raw.getChildValue(\"NAME\"))")
    @Mapping(target = "address", expression = "java(raw.getChildValue(\"ADDR\"))")
    Submitter toSubmitter(RawRecord raw);

    default List<String> mapTags(RawRecord raw, String tag) {
        return raw.getChildren(tag).stream()
                .map(RawRecord::value)
                .collect(Collectors.toList());
    }

    default List<Event> mapEvents(RawRecord raw) {
        List<String> eventTags = List.of("BIRT", "DEAT", "CHR", "BURI", "MARR", "DIV", "ADOP");
        return raw.children().stream()
                .filter(c -> eventTags.contains(c.tag()))
                .map(this::toEvent)
                .collect(Collectors.toList());
    }

    @Mapping(target = "type", source = "tag")
    @Mapping(target = "date", expression = "java(raw.getChildValue(\"DATE\"))")
    @Mapping(target = "place", expression = "java(raw.getChildValue(\"PLAC\"))")
    @Mapping(target = "description", source = "value")
    Event toEvent(RawRecord raw);
}
