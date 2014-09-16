package com.example.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseMyMessage {
	public static List<MyMessage> parse(String jsonData) {
		Type listType = new TypeToken<List<MyMessage>>(){}.getType();
		Gson gson = new Gson();
		
		List<MyMessage> messages = gson.fromJson(jsonData, listType);
		
		return messages;
	}
}
