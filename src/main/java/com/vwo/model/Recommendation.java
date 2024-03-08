package com.vwo.model;

import java.util.List;

public class Recommendation {
    private String repoName;
    private String repoBranch;
    private List<Flag> flags;

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoBranch() {
        return repoBranch;
    }

    public void setRepoBranch(String repoBranch) {
        this.repoBranch = repoBranch;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public void setFlags(List<Flag> flags) {
        this.flags = flags;
    }

    public static class Flag {
        private String flagKey;
        private String action;
        private String reason;

        public String getFlagKey() {
            return flagKey;
        }

        public void setFlagKey(String flagKey) {
            this.flagKey = flagKey;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
