package com.example.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ParseMyGroup {
	
	public static List<Group> parse(String jsonData) {
		Type listType = new TypeToken<List<Group>>(){}.getType();
		Gson gson = new Gson();
		
		List<Group> groups = gson.fromJson(jsonData, listType);
		
		return groups;
	}
	
}
