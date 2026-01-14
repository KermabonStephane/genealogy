package com.demis27.genealogy.ansel1985

import spock.lang.Specification

import java.nio.charset.Charset

class Ansel1985CharsetSpockTest extends Specification {

    def "Encode and decode a string with Spock"() {
        given:
        def charset = new Ansel1985Charset("ANSEL", new String[0])
        def originalString = "Hello, world! ŁØĐÞÆŒ±ıłøđþæœß£Ð°Ç"

        when:
        def encoded = originalString.getBytes(charset)
        def decoded = new String(encoded, charset)

        then:
        decoded == originalString
    }

    def "Test unsupported characters with Spock"() {
        given:
        def charset = new Ansel1985Charset("ANSEL", new String[0])
        def originalString = "Hello, world! 😊"

        when:
        def encoded = originalString.getBytes(charset)
        def decoded = new String(encoded, charset)

        then:
        decoded == "Hello, world! ?"
    }
}
