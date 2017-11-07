package com.github.arowshot.itemexchange.util;

import com.github.arowshot.itemexchange.exchangerules.ExchangeRule;
import com.github.arowshot.itemexchange.exchangerules.ExchangeRuleAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static final GsonBuilder gsonBuilder = new GsonBuilder()
			.registerTypeAdapter(ExchangeRule.class, new ExchangeRuleAdapter());
	
	public static Gson getGson() {
		return gsonBuilder.create();
	}
	
}
