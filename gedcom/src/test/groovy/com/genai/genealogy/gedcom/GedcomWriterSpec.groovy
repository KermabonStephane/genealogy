package com.genai.genealogy.gedcom

import com.genai.genealogy.gedcom.domain.*
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class GedcomWriterSpec extends Specification {

    def "should write a Gedcom model to UTF-8"() {
        given:
        def writer = new GedcomWriter()
        def reader = new GedcomReader()

        def header = Header.builder()
                .source(HeaderSource.builder().name("Gramps").version("6.0.6").build())
                .gedcomVersion(GedcomVersion.builder().version("5.5.1").build())
                .encoding("UTF-8")
                .build()

        def john = Individual.builder()
                .id("I1")
                .name(PersonalName.builder().name("John /Doe/").build())
                .sex("M")
                .events([Event.builder().type(GedcomTag.BIRTH.getTag()).date("1 JAN 1900").place("New York").build()])
                .familyAsSpouseIds(["F1"])
                .build()

        def jane = Individual.builder()
                .id("I2")
                .name(PersonalName.builder().name("Jane /Smith/").build())
                .sex("F")
                .familyAsSpouseIds(["F1"])
                .build()

        def family = Family.builder()
                .id("F1")
                .husbandIds(["I1"])
                .wifeIds(["I2"])
                .events([Event.builder().type(GedcomTag.MARRIAGE.getTag()).date("1 JAN 1920").build()])
                .build()

        def gedcom = Gedcom.builder()
                .header(header)
                .individuals(["I1": john, "I2": jane])
                .families(["F1": family])
                .build()

        def outputStream = new ByteArrayOutputStream()

        when:
        writer.write(gedcom, outputStream)
        def output = outputStream.toString(StandardCharsets.UTF_8)

        then:
        output.contains("0 HEAD")
        output.contains("1 CHAR UTF-8")
        output.contains("0 @I1@ INDI")
        output.contains("1 NAME John /Doe/")
        output.contains("0 @F1@ FAM")
        output.contains("1 HUSB @I1@")
        output.contains("0 TRLR")

        when: "Reading it back"
        def rereadGedcom = reader.read(new ByteArrayInputStream(output.getBytes(StandardCharsets.UTF_8)))

        then:
        rereadGedcom.individuals().size() == 2
        rereadGedcom.individuals()["I1"].name().name() == "John /Doe/"
        rereadGedcom.families()["F1"].husbandIds().contains("I1")
    }
}
