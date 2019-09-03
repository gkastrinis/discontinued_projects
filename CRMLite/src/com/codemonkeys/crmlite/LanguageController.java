package com.codemonkeys.crmlite;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * Code taken from: http://blog.cedric.ws/multiple-languages-for-your-java-application
 */
public class LanguageController {
	
	private static LanguageController instance = null;

	private Map<String, Locale> supportedLanguages;
	private ResourceBundle translation;
		
	private LanguageController(String language) {
		supportedLanguages = new HashMap<>();
		supportedLanguages.put("English", Locale.ENGLISH);
		supportedLanguages.put("Greek", new Locale("el", "GR") );
		translation = ResourceBundle.getBundle("language", supportedLanguages.get(language));
	}
	
	public String w(String keyword) {
		return translation.getString(keyword);
	}
	
	public static LanguageController getInstance() {
		if (instance != null) return instance;
		
		instance = new LanguageController(Config.language);
		return instance;
	}

}
