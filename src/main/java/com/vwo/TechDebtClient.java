package com.vwo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vwo.model.FlagDetails;
import com.vwo.utils.NetworkUtils;

public class TechDebtClient {
	// regex to identify getFlag()
    private static final String REGEX = "\\.getFlag[(]\\s*[\"']([^\"']*)[\"']\\s*[)]";
    
    // source code file extensions
    private static final String JS_FILEEXTENSION = ".js";
    
    // flag details
    // HashMap<String, FlagDetails> flagDetails = new HashMap<String, FlagDetails>();

    public static void main(String[] args) {
    	HashMap<String, FlagDetails> flagDetails_hash = new HashMap<String, FlagDetails>();
    	
    	// check params
        if (args.length != 6) {
            System.err.println("Insufficient params");
            System.exit(1);
        }

        // get the params
        String directory = args[0];
        String accountId = args[1];
        String repoName = args[2];
        String repoBranch = args[3];
        boolean isReferenceCode = Boolean.parseBoolean(args[4]);
        String apiToken = args[5];
        
        // marker
        System.out.println(accountId + " - " + directory + " - " + repoName + " - " + repoBranch + " - " + isReferenceCode);

        // get the flag details from the code base
        ArrayList<FlagDetails> flagDetails = searchSourceFiles(new File(directory), REGEX, repoName, repoBranch, isReferenceCode);
        
        // parse through the flag keys and combine common keys with added code references
        for (FlagDetails flagDetail : flagDetails) {
        	// if flag exists add code reference to existing object
        	if (flagDetails_hash.containsKey(flagDetail.getFlagKey())) {
        		flagDetails_hash.get(flagDetail.getFlagKey()).addCodeReference(flagDetail.getCodeReferences().get(0));
        	} else {
        		flagDetails_hash.put(flagDetail.getFlagKey(), flagDetail);
        	}
        }
        
        // convert the hashmap back to arraylist
        flagDetails = new ArrayList<FlagDetails>(flagDetails_hash.values());
        
        for (FlagDetails flagDetail : flagDetails) {
        	System.out.println("\nFlag : " + flagDetail.getFlagKey());
        	System.out.println("RepoName : " + flagDetail.getRepoName());
        	System.out.println("RepoBranch : " + flagDetail.getRepoBranch());
        	// System.out.println("Code References : " + flagDetail.getCodeReference().toString());
            
            // remove file content before sending to backend servers if flag not set
        	// if(!isReferenceCode)
        		// flagDetail.getCodeReference().setReferenceCode(null);
        }
        
        // send flag details to server and get response
        String response = NetworkUtils.makeHttpCall(accountId, flagDetails, apiToken);
        
        // marker
        System.out.println("Network Response : " + response);
        
        // show warnings to the user
    }
    
    // show warnings to the user
    public static void displayFlagWarnings(String response) {
    	
    }

    // search source files
    public static ArrayList<FlagDetails> searchSourceFiles(File directory, String regex, String repoName, String repoBranch, boolean isAddCodeSnippet) {
        ArrayList<FlagDetails> flagDetails = new ArrayList<FlagDetails>();
        File[] files = directory.listFiles();
        if (files != null) {
        	// parse through the files in the mentioned directory
            for (File file : files) {
                if (file.isDirectory()) {
                	// recursively search inside any sub directory
                    flagDetails.addAll(searchSourceFiles(file, regex, repoName, repoBranch, isAddCodeSnippet));
                } else if (file.getName().endsWith(JS_FILEEXTENSION)) {
                	// add files that match the mentioned file extension
                    flagDetails.addAll(searchRegexInSourceFile(file, regex, repoName, repoBranch, isAddCodeSnippet));
                }
            }
        }
        
        return flagDetails;
    }

    // search for flag in source file
    public static ArrayList<FlagDetails> searchRegexInSourceFile(File file, String regex, String repoName, String repoBranch, boolean isAddCodeSnippet) {
        ArrayList<FlagDetails> flagDetails = new ArrayList<FlagDetails>();
        Pattern pattern = Pattern.compile(regex);
        
        try {
            int lineNumber = 0;
            
            // parse through each line of the file
            for (String line : Files.readAllLines(Paths.get(file.getAbsolutePath()))) {
            	// increment line number
                lineNumber++;
                
                // if flag is found in the line, add it to the flag details
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String flagKey = matcher.group(1);
                    int charNumber = line.indexOf(flagKey);
                    
                    // set line to null if flag not set
                    line = isAddCodeSnippet ? line : null;
                    
                    // if flag exists, add to the flag's code reference
                    /* if(flagDetails.containsKey(flagKey)) {
                    	flagDetails.get(flagKey).addCodeReference(file.getName(), file.getParent(), lineNumber, charNumber, line);
                    } else {
                    	flagDetails.put(flagKey, new FlagDetails(flagKey, repoName, repoBranch, file.getName(), file.getParent(), lineNumber, charNumber, line));
                    }
                    */
                    flagDetails.add(new FlagDetails(flagKey, repoName, repoBranch, file.getName(), file.getParent(), lineNumber, charNumber, line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        
        // convert the hashmap to arraylist
        // flagDetails_arr = new ArrayList<FlagDetails>(flagDetails.values());
        
        return flagDetails;
    }
}
