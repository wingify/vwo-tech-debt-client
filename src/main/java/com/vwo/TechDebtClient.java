package com.vwo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.vwo.model.FlagDetails;
import com.vwo.parsers.FeatureFlagParser;
import com.vwo.utils.CLIUtils;
import com.vwo.utils.Constants;
import com.vwo.utils.FlagUtils;
import com.vwo.utils.GitUtils;
import com.vwo.utils.NetworkUtils;

public class TechDebtClient {
	public static ArrayList<String> featureFlags;
	public static ArrayList<FlagDetails> flagDetails;
	
	// variable flagkey assignments
	public static HashMap<String, String> flagKeyVars = new HashMap<String, String>();

    public static void main(String[] args) {
    	HashMap<String, FlagDetails> flagDetails_hash = new HashMap<String, FlagDetails>();
    	GitUtils gitUtils;
        
        // get mandatory params
        HashMap<String, String> params = CLIUtils.extractCommandLineParams(args);
        String directory = params.get(Constants.param_sourceFolder);
        String accountId = params.get(Constants.param_accountId);
        String apiToken = params.get(Constants.param_apiToken);
        
        // System.out.println(apiToken);
        
        // get optional params
        boolean isReferenceCode = params.containsKey(Constants.param_isReferenceCode) ?
        		Boolean.parseBoolean(params.get(Constants.param_isReferenceCode)) : false;
        boolean isTesting = params.containsKey(Constants.param_isTesting) ?
        		Boolean.parseBoolean(params.get(Constants.param_isTesting)) : false;
        String repoURL = params.containsKey(Constants.param_repoURL) ? params.get(Constants.param_repoURL) : null;
        String repoBranch = params.containsKey(Constants.param_repoBranch) ? params.get(Constants.param_repoBranch) : null;
        String repoName;
        
        // get git repo name and branch if not set from command line (not coming from jenkins job)
    	gitUtils = new GitUtils(directory);
        if (repoURL==null && repoBranch==null) {
        	repoName = gitUtils.getGitRepoName();
        	repoBranch = gitUtils.getGitRepoBranch();
        } else {
        	// extract repo name from repo url
        	repoName = gitUtils.extractRepoName(repoURL);
        }
        
        // marker
        System.out.println("\nStarted VWO Tech Debt Client on " + repoName + ":" + repoBranch);
        
        // get all feature flags for account
        System.out.println("Getting all feature flags for account : " + accountId);
        featureFlags = FeatureFlagParser.extractFeatureKeys(NetworkUtils.getFlagsForAccount(apiToken, isTesting));
        System.out.println("Received " + (featureFlags!=null ? featureFlags.size() : 0) + " Flags For Account");

        // get the flag details from the code base
        System.out.println("Extracting flag details from the code base");
        flagDetails = FlagUtils.searchSourceFiles(new File(directory), Constants.REGEX_getFlag, isReferenceCode, directory);
        
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
        
        // replace variables used instead of flagkeys with actual flag keys
        FlagUtils.replaceVarsWithFlagKeys(flagDetails, featureFlags, flagKeyVars);
        
        // add a deliberate delay to bypass HTTP 429
        try {
        	Thread.sleep(3000);
        } catch(InterruptedException e) {}
        
        // send flag details to the server and show recommendations to the user
        System.out.println("Recommendations for feature flags : ");
        new RecommendationHandler(NetworkUtils.sendFlagsGetRecos(accountId, repoName, repoBranch, flagDetails, apiToken, isTesting)).showRecommendation();
    }
}
