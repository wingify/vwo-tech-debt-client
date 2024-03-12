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

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitUtils {
	String sourceFolder;
	Repository gitRepo = null;

	// constructor
	public GitUtils(String sourceFolder) {
		this.sourceFolder = sourceFolder;

		/* try {

			// REMOVE
			File file = new File(this.sourceFolder);
			System.out.println("RD : sourceFolder = " + sourceFolder);
			System.out.println("RD : folder status : " + file.isDirectory());

			// build the git repository
			this.gitRepo = new FileRepositoryBuilder()
					.setGitDir(new File(sourceFolder))
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	// get git repo branch
	public String getGitRepoBranch() {
		String branch = null;

		/* try {
			branch = this.gitRepo != null ? this.gitRepo.getFullBranch() : null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		// set branch to not-found if null
		branch = branch == null ? "not-found" : branch;
		*/
		branch = CLIUtils.runProcess(new String[]{"bash", "-c", "cd " + sourceFolder + " && git branch --show-current"});

		// new String[]{"git", "branch", "--show-current"}

		return branch;
	}

	// get git repo name
	public String getGitRepoName() {
		/* String repoName = this.gitRepo != null ? this.gitRepo.getDirectory().getName() : null;

		// marker
		System.out.println("RD : repoName = " + repoName);

		// set repo name to not-found if null
		repoName = repoName == null ? "not-found" : repoName;

		return repoName;
		*/
		return "";
	}

	// extract repo name from repo url
	public String extractRepoName(String repoURL) {
		String repoName = "not-found";
		Pattern pattern = Pattern.compile(Constants.REGEX_extractRepoNameFromURL);

        // Create a Matcher object to apply the pattern to url
        Matcher matcher = pattern.matcher(repoURL);
        if (matcher.find()) {
            repoName = matcher.group(1);
        }

        return repoName;
	}
}
