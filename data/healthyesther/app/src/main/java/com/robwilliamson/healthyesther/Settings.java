package com.robwilliamson.healthyesther;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public enum Settings {
    INSTANCE;

    private static final String DEFAULT_EDIT_SCORE_EXCLUSION_LIST = "default_edit_score_exclusion_list";

    private SharedPreferences mPreferences;

    private Settings() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
    }

    public Set<String> getDefaultExcludedEditScores() {
        return mPreferences.getStringSet(DEFAULT_EDIT_SCORE_EXCLUSION_LIST, new HashSet<String>());
    }

    public void setDefaultEditScoreExclusionList(Set<String> exclusionList) {
        mPreferences.edit().putStringSet(DEFAULT_EDIT_SCORE_EXCLUSION_LIST, exclusionList).apply();
    }
}
