/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

public enum Settings {
    INSTANCE;

    private static final int DEFAULT_EDIT_SCORE_EXCLUSION_LIST = R.string.pref_default_edit_score_exclusion_list;

    private final Object mLock = new Object();
    private volatile SharedPreferences mPreferences;

    public void hideScore(@Nonnull HealthScoreTable.Row score) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.add(score.getName());
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public void hideScore(@Nonnull String scoreName) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.add(scoreName);
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public void showScore(@Nonnull HealthScoreTable.Row score) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        if (hiddenScores.remove(score.getName())) {
            setDefaultEditScoreExclusionList(hiddenScores);
        }
    }

    public void showScore(String scoreName) {
        Set<String> hiddenScores = getDefaultExcludedEditScores();
        hiddenScores.remove(scoreName);
        setDefaultEditScoreExclusionList(hiddenScores);
    }

    public Set<String> getDefaultExcludedEditScores() {
        return new HashSet<>(
                getPreferences().getStringSet(
                        string(DEFAULT_EDIT_SCORE_EXCLUSION_LIST),
                        new HashSet<String>()));
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
