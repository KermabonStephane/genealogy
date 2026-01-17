package com.genai.genealogy.gedcom.domain;

import lombok.Builder;

@Builder
public record PersonalName(String name, String prefixName, String givenName, String nickName, String surname, String surnamePrefix, String suffixName) {
}
