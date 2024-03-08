package com.vwo.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vwo.model.FlagDetails;

public class NetworkUtils {
	// send flags in the code and get the recommendations for them
    public static String sendFlagsGetRecos(String accountId, String repoName, String repoBranch, ArrayList<FlagDetails> flagDetails, String apiToken, boolean isTesting) {
        StringBuffer response = new StringBuffer();
        
        // call relaxed HTTPS validation before proceeding if testing
    	if(isTesting) {
            relaxedHTTPSValidation();
    	}
        
        try {
            // convert flag details list to JSON string
            Gson gson = new Gson();
            String jsonParams = gson.toJson(flagDetails);
            
            // add accountId, repoName, sdkKey and flag details to the params
            JsonObject json = new JsonObject();
            json.addProperty("accountId", Integer.parseInt(accountId));
            json.addProperty("repoName", repoName);
            json.addProperty("repoBranch", repoBranch);
            json.addProperty("detectedFlags", jsonParams);
            jsonParams = json.toString();
            
            // marker
            System.out.println("\nParams : " + jsonParams);
            
            // adding accountId to url
            String url = Constants.URL_GET_FLAG_RECOS.replaceAll("ACCOUNT_ID", accountId);

            // Create HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", apiToken);
            if (isTesting) {
                connection.setRequestProperty("cookie", Constants.COOKIE_DURING_TESTING);
            }
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
            // System.out.println("HTTP Response: " + response.toString());

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
	
    // get all flags for an account
    public static String getFlagsForAccount(String apiToken, boolean isTesting) {
        StringBuffer response = new StringBuffer();
    	String url = Constants.URL_GET_ALL_FLAGS;
        
        // call relaxed HTTPS validation before proceeding if testing
    	if(isTesting) {
            relaxedHTTPSValidation();
    	}
        
        try {
            // Create HTTP connection
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token", apiToken);
            if(isTesting) {
                connection.setRequestProperty("cookie", Constants.COOKIE_DURING_TESTING);
            }

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
    
    // relaxed https validation for test app
    private static class RelaxedHostnameVerifier implements HostnameVerifier {
        // @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    public static void relaxedHTTPSValidation() {
    	// Create a TrustManager that trusts all certificates
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) {}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) {}
                public X509Certificate[] getAcceptedIssuers() { return null; }
            }
        };

        // Create SSLContext with relaxed SSL validation
		try {
	        SSLContext sslContext;
			sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());
	        
	        // Set the default SSL socket factory and hostname verifier
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new RelaxedHostnameVerifier());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
    }
}
