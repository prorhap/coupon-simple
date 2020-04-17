package com.github.prorhap.coupon.simple.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class JsonDateDeserializer extends JsonDeserializer<Date> {

    private FastDateFormat format = FastDateFormat.getInstance("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            return format.parse(jsonParser.getText());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
