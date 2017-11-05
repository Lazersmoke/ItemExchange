package com.devotedmc.itemexchange.util;

import com.devotedmc.itemexchange.exchangerules.ExchangeRule;
import com.devotedmc.itemexchange.exchangerules.ExchangeRuleAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static final GsonBuilder gsonBuilder = new GsonBuilder()
			.registerTypeAdapter(ExchangeRule.class, new ExchangeRuleAdapter());
	
	public static Gson getGson() {
		return gsonBuilder.create();
	}
	
}
