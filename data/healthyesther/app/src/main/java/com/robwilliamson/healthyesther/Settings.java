package com.robwilliamson.healthyesther;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public enum Settings {
    INSTANCE;

    private static final int DEFAULT_EDIT_SCORE_EXCLUSION_LIST = R.string.pref_default_edit_score_exclusion_list;

    private SharedPreferences mPreferences;

    private Settings() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance().getApplicationContext());
    }

    public Set<String> getDefaultExcludedEditScores() {
        return mPreferences.getStringSet(string(DEFAULT_EDIT_SCORE_EXCLUSION_LIST), new HashSet<String>());
    }

    public void setDefaultEditScoreExclusionList(Set<String> exclusionList) {
        mPreferences.edit().putStringSet(string(DEFAULT_EDIT_SCORE_EXCLUSION_LIST), exclusionList).apply();
    }

    private String string(int id) {
        return App.getInstance().getString(id);
    }
}
