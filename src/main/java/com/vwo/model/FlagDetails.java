package com.vwo.model;

import java.util.ArrayList;

public class FlagDetails {
	private String flagKey;
	private ArrayList<CodeReference> codeReferences = new ArrayList<FlagDetails.CodeReference>();
	
	// constructor
	public FlagDetails(String flagKey, String fileName, String fileLocation, int lineNumber, int charNumber, String referenceCode) {
		this.flagKey = flagKey;
		this.codeReferences.add(new CodeReference(fileName, fileLocation, lineNumber, charNumber, referenceCode));
	}

	public String getFlagKey() {
		return flagKey;
	}

	public void setFlagKey(String flagKey) {
		this.flagKey = flagKey;
	}

	public ArrayList<CodeReference> getCodeReferences() {
		return codeReferences;
	}

	public void setCodeReferences(ArrayList<CodeReference> codeReferences) {
		this.codeReferences = codeReferences;
	}
	
	// add code reference to an existing flag
	public void addCodeReference(String fileName, String fileLocation, int lineNumber, int charNumber, String referenceCode) {
		this.codeReferences.add(new CodeReference(fileName, fileLocation, lineNumber, charNumber, referenceCode));
	}
	public void addCodeReference(CodeReference codeReference) {
		this.codeReferences.add(codeReference);
	}
	
	
	
	// code reference for the flag
	public class CodeReference {
	    private String fileName;
	    private String fileLocation;
	    private int lineNumber;
	    private int charNumber;
	    private String referenceCode;

	    // constructor
	    public CodeReference(String fileName, String fileLocation, int lineNumber, int charNumber, String referenceCode) {
	        this.fileName = fileName;
	        this.fileLocation = fileLocation;
	        this.lineNumber = lineNumber;
	        this.charNumber = charNumber;
	        this.referenceCode = referenceCode;
	    }

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileLocation() {
			return fileLocation;
		}

		public void setFileLocation(String fileLocation) {
			this.fileLocation = fileLocation;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}

		public int getCharNumber() {
			return charNumber;
		}

		public void setCharNumber(int charNumber) {
			this.charNumber = charNumber;
		}

		public String getReferenceCode() {
			return referenceCode.trim();
		}

		public void setReferenceCode(String referenceCode) {
			this.referenceCode = referenceCode;
		}
	}
}