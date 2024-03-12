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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vwo.TechDebtClient;
import com.vwo.model.FlagDetails;

public class FlagUtils {
    // substitute flag keys for variables in flag details
    public static void replaceVarsWithFlagKeys(ArrayList<FlagDetails> flagDetails, ArrayList<String> featureFlags, HashMap<String, String> flagKeyVars) {
    	HashMap<String, String> featureFlags_hash = new HashMap<String, String>();

    	// convert all feature flags in account to hashmap
    	for(String flag : featureFlags) {
    		featureFlags_hash.put(flag, "");
    	}

    	// parse through all flag details and check for unknown flags and check if they are variables, then substitute with actual flag
    	for(FlagDetails flagDetail : flagDetails) {
    		if(!featureFlags_hash.containsKey(flagDetail.getFlagKey())) {
    			if(flagKeyVars.containsKey(flagDetail.getFlagKey())) {
    				flagDetail.setFlagKey(flagKeyVars.get(flagDetail.getFlagKey()));
    			}
    		}
    	}
    }

    // search source files
    public static ArrayList<FlagDetails> searchSourceFiles(File directory, String regex, boolean isAddCodeSnippet, String sourceDirectory) {
        ArrayList<FlagDetails> flagDetails = new ArrayList<FlagDetails>();
        File[] files = directory.listFiles();
        if (files != null) {
        	// parse through the files in the mentioned directory
            for (File file : files) {
                if (file.isDirectory()) {
                	// recursively search inside any sub directory
                    flagDetails.addAll(searchSourceFiles(file, regex, isAddCodeSnippet, sourceDirectory));
                } else if (file.getName().endsWith(Constants.FILEEXTENSION_JS) && !file.getAbsoluteFile().toString().contains(Constants.excluseList)) {
                	// add files that match the mentioned file extension
                    flagDetails.addAll(searchRegexInSourceFile(file, regex, isAddCodeSnippet, sourceDirectory));
                }
            }
        }

        return flagDetails;
    }

    // search for flag in source file
    public static ArrayList<FlagDetails> searchRegexInSourceFile(File file, String regex, boolean isAddCodeSnippet, String directory) {
        ArrayList<FlagDetails> flagDetails = new ArrayList<FlagDetails>();
        FlagDetails flagDetail;
        Pattern pattern;
        Matcher matcher;
        String fileLocation;
        String line;

        try {
            int lineNumber = 0;

            // parse through each line of the file
            BufferedReader reader = new BufferedReader(new FileReader(file));
            // for (String line : Files.readAllLines(Paths.get(file.getAbsolutePath()))) {
            while ((line=reader.readLine()) != null) {
            	// increment line number
                lineNumber++;

                // if flag is found in the line, add it to the flag details
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String flagKey = matcher.group(1);
                    int charNumber = line.indexOf(flagKey);

                    // set line to null if flag not set
                    // line = isAddCodeSnippet ? line : null;

                    // only retain the relative path in file location
                    fileLocation = file.getParent();
                    if (fileLocation.indexOf(directory) == 0) {
                    	fileLocation = fileLocation.substring(directory.length());

                    	// in case of root folder, show "/" instead of blank
                    	fileLocation = fileLocation.length()==0 ? "/" : fileLocation;
                    }

                    // add to arraylist
                    flagDetails.add(new FlagDetails(flagKey, file.getName(), fileLocation, lineNumber, charNumber, line));
                }

                // parse through the flag keys and find variable assignments for them
                for (String flag : TechDebtClient.featureFlags) {
                	pattern = Pattern.compile(Constants.REGEX_getVariable.replaceAll("FLAG_NAME", flag));
                	matcher = pattern.matcher(line);
                	while(matcher.find()) {
                		TechDebtClient.flagKeyVars.put(matcher.group(0).split("=")[0].trim(), flag);
                	}
                }

                // if code references are not to be sent, remove them form the flag details
                for (int x=0; !isAddCodeSnippet && flagDetails!=null && x<flagDetails.size(); x++) {
                	flagDetail = flagDetails.get(x);
                	for(int y=0; flagDetail.getCodeReferences()!=null && y<flagDetail.getCodeReferences().size(); y++) {
                		flagDetail.getCodeReferences().get(y).setReferenceCode(null);
                	}
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return flagDetails;
    }
}
