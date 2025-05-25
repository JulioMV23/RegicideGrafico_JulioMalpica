package org.example.regicidegrafico_juliomalpica;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CartaAdapter implements JsonSerializer<Carta>, JsonDeserializer<Carta> {

    @Override
    public JsonElement serialize(Carta src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.addProperty("type", src.getClass().getSimpleName());
        return jsonObject;
    }

    @Override
    public Carta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if ("CartaNormal".equals(type)) {
            return context.deserialize(json, CartaNormal.class);
        }

        throw new JsonParseException("Tipo desconocido de carta: " + type);
    }
}
