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

package com.vwo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CLIUtils {
	// extract params from command line
	public static HashMap<String, String> extractCommandLineParams(String[] args) {
		HashMap<String, String> params = new HashMap<String, String>();

		// parse through params and extract as key value pairs
		for(String param : args) {
			params.put(param.split("=")[0], param.split("=")[1]);
		}

		return params;
	}

	// run a process and return its output
	public static String runProcess(String[] commands) {
		StringBuilder output = new StringBuilder();

		try {
	        // Create ProcessBuilder
	        ProcessBuilder processBuilder = new ProcessBuilder(commands);

	        // Start the process and read the output
	        Process process = processBuilder.start();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            output.append(line).append("\n");
	        }

	        // Wait for the process to finish
	        int exitCode = process.waitFor();

	        // remove new line char from the end, if any
	        if(output.length()>0 && output.charAt(output.length()-1)=='\n') {
	        	output.deleteCharAt(output.length()-1);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (InterruptedException e) {
	    	e.printStackTrace();
	    }

		return output.toString();
	}
}
