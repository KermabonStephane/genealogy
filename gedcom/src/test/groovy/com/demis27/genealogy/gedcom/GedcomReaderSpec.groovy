package com.demis27.genealogy.gedcom

import com.demis27.genealogy.gedcom.domain.Family
import com.demis27.genealogy.gedcom.domain.Individual
import com.demis27.genealogy.gedcom.domain.Note
import spock.lang.Specification

class GedcomReaderSpec extends Specification {

    def "should read a GEDCOM file with only header"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/samples/header.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.header != null
        gedcom.header.source.name == "Gramps"
        gedcom.header.source.version == "6.0.6"
        gedcom.header.gedcomVersion.version == "5.5.1"
        gedcom.header.encoding == "UTF-8"
        gedcom.header.file == "/Users/stephanekermabon/Perso/genai/kermabon.ged"
        gedcom.header.language == "English"
        gedcom.header.copyright == "Copyright (c) 2026 ."
        gedcom.header.submitter.id == "@SUBM@"
    }

    def "should read a GEDCOM individual only"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/samples/individual.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.individuals.size() == 1
        Individual josephine = gedcom.individuals().values().first()
        josephine != null
        josephine.id == "I0000"
        josephine.name.name == "Joséphine Julienne /Devin/"
        josephine.name.givenName == "Joséphine Julienne"
        josephine.name.surname == "Devin"
        josephine.sex == "F"
    }

    def "should read a GEDCOM family only"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/samples/family.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.families.size() == 1
        Family family = gedcom.families().values().first()
        family != null
        family.id == "F0218"
        family.husbandIds.size() == 1
        family.husbandIds.first() == "I0093"
        family.wifeIds.size() == 1
        family.wifeIds.first() == "I0000"
    }

    def "should read a GEDCOM note only"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/samples/note.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.notes().size() == 1
        Note note = gedcom.notes().values().first()
        note != null
        note.id == "N0001"
        note.content.startsWith("Le deux décembre mil neuf cent vingt sept, vingt heures");
    }

    def "should read complete GEDCOM file"() {
        given:
        def reader = new GedcomReader()
        def inputStream = getClass().getResourceAsStream("/complete.ged")

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.individuals.size() == 643
        gedcom.families.size() == 242
        gedcom.notes.size() == 2
        gedcom.submitters.size() == 1
        gedcom.sources.size() == 3
    }
}
