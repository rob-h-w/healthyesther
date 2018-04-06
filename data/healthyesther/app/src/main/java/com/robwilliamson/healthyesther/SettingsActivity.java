/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = false;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Determines whether the simplified settings UI should be shown. This is
     * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
     * doesn't have newer APIs like {@link PreferenceFragment}, or the device
     * doesn't have an extra-large screen. In these cases, a single-pane
     * "simplified" settings UI should be shown.
     */
    private static boolean isSimplePreferences(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                || !isXLargeTablet(context);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        if (!isSimplePreferences(this)) {
            return;
        }

        // In the simplified UI, fragments are not used at all and we instead
        // use the older PreferenceActivity APIs.

        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        MultiSelectListPreference hiddenScores = new MultiSelectListPreference(this);
        hiddenScores.setKey(getString(R.string.pref_default_edit_score_exclusion_list));
        hiddenScores.setTitle(R.string.hidden_scores);
        hiddenScores.setSummary(R.string.hidden_scores_summary);
        hiddenScores.setDefaultValue(new HashSet<String>());
        hiddenScores.setPersistent(false);
        updateExcludedScoresPreference(hiddenScores);

        hiddenScores.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                @SuppressWarnings("unchecked")
                Set<String> modifiedValues = (Set<String>) newValue;

                final String hidden = getString(R.string.hidden);

                for (String value : modifiedValues) {
                    String title = value.substring(hidden.length());
                    Settings.INSTANCE.showScore(title);
                }

                MultiSelectListPreference hiddenScores = (MultiSelectListPreference) preference;
                updateExcludedScoresPreference(hiddenScores);

                return false;
            }
        });

        PreferenceScreen screen = getPreferenceScreen();
        screen.addPreference(hiddenScores);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this) && !isSimplePreferences(this);
    }

    private void updateExcludedScoresPreference(final MultiSelectListPreference hiddenScores) {
        Set<String> values = Settings.INSTANCE.getDefaultExcludedEditScores();
        String[] entries = new String[values.size()];
        String[] entryValues = new String[values.size()];
        entries = values.toArray(entries);

        final String hidden = getString(R.string.hidden);

        for (int i = 0; i < values.size(); i++) {
            entryValues[i] = hidden + entries[i];
        }

        hiddenScores.setEnabled(!(entries.length == 0));
        hiddenScores.setDialogTitle(R.string.show_hidden_scores);
        hiddenScores.setEntries(entries);
        hiddenScores.setEntryValues(entryValues);
    }
}
