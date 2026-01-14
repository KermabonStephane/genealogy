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
}
