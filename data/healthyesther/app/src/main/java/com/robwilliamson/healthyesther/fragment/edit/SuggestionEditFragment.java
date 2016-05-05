/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.db.includes.BaseRow;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Use with fragments that have an auto complete box with suggestions.
 */
public abstract class SuggestionEditFragment<R extends BaseRow> extends EditFragment<R> {
    private static final String SUGGESTION_IDS = "suggestion ids";
    private Map<String, Long> mSuggestionIds;

    @Override
    public void onResume() {
        super.onResume();

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onNameChanged();
            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNameClicked();
            }
        });

        setSuggestionIdsOnUi();
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            //noinspection unchecked
            mSuggestionIds = (Map<String, Long>) savedInstanceState.getSerializable(SUGGESTION_IDS);
        }
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(SUGGESTION_IDS, (Serializable) mSuggestionIds);
    }

    protected void onNameChanged() {
        updateAttachedActivity();
    }

    protected void onNameClicked() {
        updateAttachedActivity();
    }

    protected void setSuggestionIds(Map<String, Long> suggestionIds) {
        mSuggestionIds = suggestionIds;

        if (getActivity() != null) {
            setSuggestionIdsOnUi();
        }
    }

    private void setSuggestionIdsOnUi() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (null == mSuggestionIds) {
                    getNameView().setAdapter(null);
                    return;
                }

                Set<String> set = mSuggestionIds.keySet();
                String[] suggestions = new String[set.size()];
                set.toArray(suggestions);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        suggestions);

                AutoCompleteTextView nameView = getNameView();

                if (nameView != null) {
                    getNameView().setAdapter(adapter);
                }
            }
        });
    }

    protected Long getSuggestionId(String name) {
        return mSuggestionIds == null ? null : mSuggestionIds.get(name);
    }

    protected abstract AutoCompleteTextView getNameView();
}
