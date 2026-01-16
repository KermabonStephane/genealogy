package com.genai.genealogy.gedcom

import spock.lang.Specification

class GedcomReaderSpec extends Specification {

    def "should read a simple GEDCOM file"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/sample.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.header != null
        gedcom.header.encoding == "UTF-8"
        gedcom.header.gedcomVersion == "5.5"

        and:
        gedcom.individuals.size() == 2
        def john = gedcom.individuals["I1"]
        john.name == "John /Doe/"
        john.sex == "M"
        john.events.size() == 1
        john.events[0].type == "BIRT"
        john.events[0].date == "1 JAN 1900"
        john.events[0].place == "New York, USA"

        and:
        gedcom.families.size() == 1
        def fam = gedcom.families["F1"]
        fam.husbandId == "@I1@"
        fam.wifeId == "@I2@"
        fam.events.size() == 1
        fam.events[0].type == "MARR"
        fam.events[0].date == "1 JAN 1920"
    }

    def "should read a GEDCOM file with only header"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/samples/header.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.header != null
        gedcom.header.source == "Gramps"
        gedcom.header.version == "6.0.6"
        gedcom.header.gedcomVersion == "5.5.1"
        gedcom.header.encoding == "UTF-8"
        gedcom.header.date == "16 JAN 2026"
        gedcom.header.time == "09:56:38"
        gedcom.header.submitterId == "@SUBM@"

        and:
        gedcom.individuals.isEmpty()
        gedcom.families.isEmpty()
        gedcom.sources.isEmpty()
        gedcom.submitters.size() == 1
        gedcom.submitters["SUBM"].name == "John /Doe/"
    }
}
