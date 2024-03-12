/**
 * Copyright 2024 Wingify Software Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
