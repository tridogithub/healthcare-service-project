package com.trido.healthcare.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.trido.healthcare.entity.enumm.Gender;

import java.io.IOException;

public class GenderToStringSerializer extends JsonSerializer<Gender> {
    @Override
    public void serialize(Gender gender, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String result = gender.equals(Gender.Male) ? "M" : (gender.equals(Gender.Female) ? "F" : "O");
        jsonGenerator.writeString(result);
    }
}
