package com.genai.genealogy.gedcom.parser

import com.genai.genealogy.gedcom.domain.GedcomTag
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class GedcomParserSpec extends Specification {

    def "should parse levels correctly"() {
        given:
        def parser = new GedcomParser()
        def gedcomText = """0 HEAD
1 CHAR UTF-8
1 GEDC
2 VERS 5.5
0 @I1@ INDI
1 NAME John /Doe/
2 GIVN John
2 SURN Doe"""
        def inputStream = new ByteArrayInputStream(gedcomText.getBytes("UTF-8"))

        when:
        def records = parser.parse(inputStream, StandardCharsets.UTF_8)

        then:
        records.size() == 2
        records[0].tag == GedcomTag.HEAD.getTag()
        records[0].children.size() == 2
        records[0].children[1].tag == GedcomTag.GEDCOM.getTag()
        records[0].children[1].children.size() == 1
        records[0].children[1].children[0].tag == GedcomTag.VERSION.getTag()
        records[0].children[1].children[0].value == "5.5"

        and:
        records[1].tag == GedcomTag.INDIVIDUAL.getTag()
        records[1].id == "I1"
        records[1].children.size() == 1
        records[1].children[0].tag == GedcomTag.NAME.getTag()
        records[1].children[0].value == "John /Doe/"
        records[1].children[0].children.size() == 2
        records[1].children[0].children[0].tag == GedcomTag.GIVEN_NAME.getTag()
        records[1].children[0].children[0].value == "John"
    }
}
