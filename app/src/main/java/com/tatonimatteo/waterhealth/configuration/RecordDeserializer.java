package com.tatonimatteo.waterhealth.configuration;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.tatonimatteo.waterhealth.entity.Record;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RecordDeserializer implements JsonDeserializer<Record> {

    @Override
    public Record deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Deserializzazione di day e time
        String day = jsonObject.get("day").getAsString();
        String time = jsonObject.get("time").getAsString();
        LocalDateTime dateTime = LocalDateTime.parse(day + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Costruzione di un oggetto Record
        long id = jsonObject.get("id").getAsLong();
        long stationID = jsonObject.get("stationId").getAsLong();
        long sensorID = jsonObject.get("sensorId").getAsLong();
        double value = jsonObject.get("value").getAsDouble();
        return new Record(id, dateTime, stationID, sensorID, value);
    }
}

