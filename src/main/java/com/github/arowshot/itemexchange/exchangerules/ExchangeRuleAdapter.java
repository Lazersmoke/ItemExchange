package com.github.arowshot.itemexchange.exchangerules;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ExchangeRuleAdapter implements JsonSerializer<ExchangeRule>, JsonDeserializer<ExchangeRule> {

	public ExchangeRule deserialize(JsonElement el, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = el.getAsJsonObject();
		String className = jsonObject.get("class").getAsString();
		
		Class<?> klass = null;
		try {
			klass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new JsonParseException(e.getMessage());
		}
		return context.deserialize(jsonObject.get("instance"), klass);
	}

	public JsonElement serialize(ExchangeRule src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject retValue = new JsonObject();
		retValue.addProperty("class", src.getClass().getName());
		retValue.add("instance", context.serialize(src));
		return retValue;
	}

}
