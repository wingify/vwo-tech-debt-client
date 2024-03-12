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

public class Constants {
	// regex
    // public static final String REGEX_getFlag = "\\.getFlag[(]\\s*[\"']?([^\"']*?)[\"']?\\s*[)]"; // flag key is a variable in .getFlag(FLAG_KEY)
	// public static final String REGEX_getFlag = "\\.getFlag[(]\\s*[\"']?([^\"']*?)[\"']?\\s*[)]";
	// public static final String REGEX_getFlag = "\\.getFlag[(]\\s*[\"']?([^\"']*?)[\"']?\\s*";
	public static final String REGEX_getFlag = "\\.getFlag\\(\\s*(?:'([^']+)'|([^),]+))";

    public static final String REGEX_getVariable = "(\\w+)\\s*=\\s*\"FLAG_NAME\"";
    public static final String REGEX_extractRepoNameFromURL = "([^/]+)\\.git$";

    // params
    public static final String param_sourceFolder = "sourceFolder";
    public static final String param_accountId = "accountId";
    public static final String param_isReferenceCode = "isReferenceCode";
    public static final String param_apiToken = "APIToken";
    public static final String param_repoBranch = "repoBranch";
    public static final String param_repoURL = "repoURL";
    public static final String param_isTesting = "isTesting";

    // files exclude list
    public static final String excluseList = "node_modules";

    // source code file extensions
    public static final String FILEEXTENSION_JS = ".js";

	// cookie used during testing
    public static final String COOKIE_DURING_TESTING = "";
	public static final String API_TOKEN_DURING_TESTING = "";

	// urls
	// public static final String URL_GET_ALL_FLAGS = "https://vwotestapp14.vwo.com/api/v2/accounts/current/features?meta=1";
	// public static final String URL_GET_FLAG_RECOS = "https://vwotestapp14.vwo.com/api/v2/accounts/ACCOUNT_ID/features/tech-debt";
	public static final String URL_GET_ALL_FLAGS = "https://app.vwo.com/api/v2/accounts/current/features?meta=1";
	public static final String URL_GET_FLAG_RECOS = "https://app.vwo.com/api/v2/accounts/ACCOUNT_ID/features/tech-debt";
}
