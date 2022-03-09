package com.trido.healthcare.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.trido.healthcare.entity.enumm.Gender;

import java.io.IOException;

public class GenderDeserializer extends JsonDeserializer<Gender> {
    @Override
    public Gender deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if ("F".equals(text) && "M".equals(text) && "O".equals(text)) {
            return (Gender) deserializationContext.handleUnexpectedToken(Gender.class, jsonParser);
        }
        return Gender.getGenderFromCode(text);
    }
}
