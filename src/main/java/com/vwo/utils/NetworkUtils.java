package com.vwo.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vwo.model.FlagDetails;

public class NetworkUtils {
	static final String URL = "https://abc.com";
	
    public static String makeHttpCall(String accountId, ArrayList<FlagDetails> flagDetails, String apiToken) {
        StringBuffer response = new StringBuffer();
        
        try {
            // convert flag details list to JSON string
            Gson gson = new Gson();
            String jsonParams = gson.toJson(flagDetails);
            
            // add accountId, repoName, sdkKey and flag details to the params
            JsonObject json = new JsonObject();
            json.addProperty("accountId", Integer.parseInt(accountId));
            json.addProperty("detectedFlags", jsonParams);
            jsonParams = json.toString();
            
            // marker
            System.out.println("\nParams : " + jsonParams);

            // Create HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", apiToken);
            connection.setDoOutput(true);

            // Send JSON string as parameters
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonParams);
            outputStream.flush();
            outputStream.close();

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Print response
            System.out.println("HTTP Response: " + response.toString());

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
}
