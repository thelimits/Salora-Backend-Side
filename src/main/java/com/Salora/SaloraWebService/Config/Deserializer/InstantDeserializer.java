package com.Salora.SaloraWebService.Config.Deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class InstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String timestamp = parser.getValueAsString();
        OffsetDateTime utcDateTime = OffsetDateTime.parse(timestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime asiaDateTime = utcDateTime.atZoneSameInstant(ZoneId.of("Asia/Jakarta")); // zona waktu jakarta
        return asiaDateTime.toInstant();
    }
}
