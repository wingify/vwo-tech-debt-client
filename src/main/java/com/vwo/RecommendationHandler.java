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

package com.vwo;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.vwo.model.Recommendation;

public class RecommendationHandler {
	Recommendation recommendation;

	// constructor
	public RecommendationHandler(String recommendations_jsonStr) {
		JSONObject recommendations_json;

		this.recommendation = null;

		// extract the data from server response
		if(recommendations_jsonStr!=null && recommendations_jsonStr.length()>0) {
			recommendations_json = new JSONObject(recommendations_jsonStr);

			// extract the data if exists and from it create the recommendations object
			if(recommendations_json.has("_data")) {
				this.recommendation = new Gson().fromJson(recommendations_json.getJSONObject("_data").toString(), Recommendation.class);
			}
		}
	}

	// show recommendation
	public void showRecommendation() {
		int count = 1;

		// print generic header
		System.out.println("\nVWO Recommendations :");

		// proceed only if there are recommendations
		if(this.recommendation != null) {
			// Common details
	        System.out.println("Repo Name : " + recommendation.getRepoName());
	        System.out.println("Repo Branch : " + recommendation.getRepoBranch());

	        // parse through the flags and print their details
	        for (Recommendation.Flag flag : recommendation.getFlags()) {
	            System.out.println("\n" + count++ + ". Flag Key : " + flag.getFlagKey());
	            System.out.println("Recommendation : " + flag.getAction());
	            System.out.println("Reason : " + flag.getReason());
	        }
		} else {
			System.out.println("No Recommendations.");
		}
	}
}
