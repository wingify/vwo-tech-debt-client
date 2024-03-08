package com.vwo.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeatureFlagParser {
	// get all feature keys
	public static ArrayList<String> extractFeatureKeys(String json) {
	    ArrayList<String> featureKeys = new ArrayList<String>();
	    JSONObject jsonObject;
	    JSONArray dataArray;
	    
	    // create json objects
	    if (json!=null && json.length()>0) {
	    	jsonObject = new JSONObject(json);
	    	if (jsonObject.has("_data")) {
	    		dataArray = jsonObject.getJSONArray("_data");
	    		
	    	    // parse through the response
	    	    for (int i = 0; i < dataArray.length(); i++) {
	    	        JSONObject dataObject = dataArray.getJSONObject(i);
	    	        String featureKey = dataObject.optString("featureKey", null);
	    	        if (featureKey != null && !featureKey.isEmpty()) {
	    	            featureKeys.add(featureKey);
	    	        }
	    	    }
	    	}
	    }
	
	    return featureKeys;
	}
}
