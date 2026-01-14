package com.genai.genealogy.gedcom

import spock.lang.Specification
import java.nio.charset.Charset

class AnselReaderSpec extends Specification {

    def "should read a GEDCOM file with ANSEL encoding and match UTF-8 content"() {
        given:
        def reader = new GedcomReader()
        
        // Constructing a GEDCOM file in ANSEL bytes that matches the content of sample.ged
        // sample.ged content:
        // 0 HEAD
        // 1 SOUR PAF
        // 2 VERS 2.1
        // 1 GEDC
        // 2 VERS 5.5
        // 1 CHAR UTF-8  <-- We'll change this to ANSEL
        // 0 @I1@ INDI
        // 1 NAME John /Doe/
        // 1 SEX M
        // 1 BIRT
        // 2 DATE 1 JAN 1900
        // 2 PLAC New York, USA
        // 1 FAMS @F1@
        // 0 @I2@ INDI
        // 1 NAME Jane /Smith/
        // 1 SEX F
        // 1 FAMS @F1@
        // 0 @F1@ FAM
        // 1 HUSB @I1@
        // 1 WIFE @I2@
        // 1 MARR
        // 2 DATE 1 JAN 1920
        // 0 TRLR

        def anselBytes = []
        anselBytes.addAll("0 HEAD\n".getBytes("ASCII"))
        anselBytes.addAll("1 SOUR PAF\n".getBytes("ASCII"))
        anselBytes.addAll("2 VERS 2.1\n".getBytes("ASCII"))
        anselBytes.addAll("1 GEDC\n".getBytes("ASCII"))
        anselBytes.addAll("2 VERS 5.5\n".getBytes("ASCII"))
        anselBytes.addAll("1 CHAR ANSEL\n".getBytes("ASCII"))
        anselBytes.addAll("0 @I1@ INDI\n".getBytes("ASCII"))
        anselBytes.addAll("1 NAME John /Doe/\n".getBytes("ASCII"))
        anselBytes.addAll("1 SEX M\n".getBytes("ASCII"))
        anselBytes.addAll("1 BIRT\n".getBytes("ASCII"))
        anselBytes.addAll("2 DATE 1 JAN 1900\n".getBytes("ASCII"))
        anselBytes.addAll("2 PLAC New York, USA\n".getBytes("ASCII"))
        anselBytes.addAll("1 FAMS @F1@\n".getBytes("ASCII"))
        anselBytes.addAll("0 @I2@ INDI\n".getBytes("ASCII"))
        anselBytes.addAll("1 NAME Jane /Smith/\n".getBytes("ASCII"))
        anselBytes.addAll("1 SEX F\n".getBytes("ASCII"))
        anselBytes.addAll("1 FAMS @F1@\n".getBytes("ASCII"))
        anselBytes.addAll("0 @F1@ FAM\n".getBytes("ASCII"))
        anselBytes.addAll("1 HUSB @I1@\n".getBytes("ASCII"))
        anselBytes.addAll("1 WIFE @I2@\n".getBytes("ASCII"))
        anselBytes.addAll("1 MARR\n".getBytes("ASCII"))
        anselBytes.addAll("2 DATE 1 JAN 1920\n".getBytes("ASCII"))
        anselBytes.addAll("0 TRLR\n".getBytes("ASCII"))

        def inputStream = new ByteArrayInputStream(anselBytes as byte[])

        when:
        def gedcom = reader.read(inputStream)

        then:
        gedcom != null
        gedcom.header.encoding == "ANSEL"
        
        and: "Individual content should match"
        gedcom.individuals.size() == 2
        gedcom.individuals["I1"].name == "John /Doe/"
        gedcom.individuals["I2"].name == "Jane /Smith/"

        and: "Family content should match"
        gedcom.families.size() == 1
        gedcom.families["F1"].husbandId == "@I1@"
        gedcom.families["F1"].wifeId == "@I2@"
    }
}
