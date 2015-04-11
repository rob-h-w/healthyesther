package com.robwilliamson.healthyesther;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.robwilliamson.healthyesther.db.definition.HealthScore;

import java.util.HashSet;
import java.util.Set;

public enum Settings {
    INSTANCE;

    private static final int DEFAULT_EDIT_SCORE_EXCLUSION_LIST = R.string.pref_default_edit_score_exclusion_list;

    private Object mLock = new Object();
    private volatile SharedPreferences mPreferences;

    public void hideScore(HealthScore.Score score) {
        if (score == null) {
            return;
        }

        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.add(score.name);
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public void hideScore(String scoreName) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.add(scoreName);
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public void showScore(HealthScore.Score score) {
        if (score == null) {
            return;
        }

        Set<String> hiddenScores = getDefaultExcludedEditScores();
        if (hiddenScores.remove(score.name)) {
            setDefaultEditScoreExclusionList(hiddenScores);
        }
    }

    public void showScore(String scoreName) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.remove(scoreName);
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public Set<String> getDefaultExcludedEditScores() {
        Set<String> exclusionSet = new HashSet<>(
                getPreferences().getStringSet(
                        string(DEFAULT_EDIT_SCORE_EXCLUSION_LIST),
                        new HashSet<String>()));
        return exclusionSet;
    }

    public void resetExclusionList() {
        setDefaultEditScoreExclusionList(new HashSet<String>());
    }

    private void setDefaultEditScoreExclusionList(Set<String> exclusionList) {
        SharedPreferences.Editor edit = getPreferences().edit();
        final String key = string(DEFAULT_EDIT_SCORE_EXCLUSION_LIST);
        edit.remove(key);
        edit.putStringSet(key, exclusionList).apply();
    }

    private String string(int id) {
        return App.getInstance().getString(id);
    }

    private SharedPreferences getPreferences() {
        synchronized (mLock) {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
            }

            return mPreferences;
        }
    }
}
