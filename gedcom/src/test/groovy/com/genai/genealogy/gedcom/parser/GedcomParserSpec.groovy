package com.genai.genealogy.gedcom.parser

import spock.lang.Specification

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
        def records = parser.parse(inputStream, java.nio.charset.StandardCharsets.UTF_8)

        then:
        records.size() == 2
        records[0].tag == "HEAD"
        records[0].children.size() == 2
        records[0].children[1].tag == "GEDC"
        records[0].children[1].children.size() == 1
        records[0].children[1].children[0].tag == "VERS"
        records[0].children[1].children[0].value == "5.5"

        and:
        records[1].tag == "INDI"
        records[1].id == "I1"
        records[1].children.size() == 1
        records[1].children[0].tag == "NAME"
        records[1].children[0].value == "John /Doe/"
        records[1].children[0].children.size() == 2
        records[1].children[0].children[0].tag == "GIVN"
        records[1].children[0].children[0].value == "John"
    }
}
