/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractAddNamedDialogFragment extends DialogFragment {

    private static final String SUGGESTIONS = "suggestions";
    private HashMap<String, Long> mSuggestions = null;
    private boolean mInflatedContent = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && mSuggestions == null) {
            mSuggestions = Utils.Bundles.get(savedInstanceState, SUGGESTIONS, new Utils.Bundles.HashGetter<Long>() {
                @Override
                public Long get(Bundle bundle, String bundleKey) {
                    long value = bundle.getLong(bundleKey);
                    return value == 0L ? null : value;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_value, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        View view = Utils.checkNotNull(getView());
        Context context = Utils.checkNotNull(view.getContext());
        getNameTitle().setText(context.getText(valueNameId()));

        AutoCompleteTextView name = getNameTextView();
        name.setCompletionHint(getView().getContext().getText(valueCompletionHintId()));
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                AbstractAddNamedDialogFragment.this.newNameEntered(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suggestion = (String) parent.getAdapter().getItem(position);
                AbstractAddNamedDialogFragment.this.suggestionSelected(suggestion, mSuggestions.get(suggestion));
            }
        });
        getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk();
                dismiss();
            }
        });

        if (contentLayoutId() != null && !mInflatedContent) {
            View.inflate(getView().getContext(), contentLayoutId(), getContentArea());
            mInflatedContent = true;
        }

        updateSuggestionAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mSuggestions != null) {
            Utils.Bundles.put(outState, SUGGESTIONS, mSuggestions, new Utils.Bundles.HashPutter() {
                @Override
                public void put(Bundle bundle, String bundleKey, String key) {
                    Long value = mSuggestions.get(key);
                    if (value != null) {
                        bundle.putLong(bundleKey, value);
                    }
                }
            });
        }
    }

    protected String getName() {
        return getNameTextView().getText().toString();
    }

    protected void setName(String name) {
        getNameTextView().setText(name);
    }

    protected void setValid(boolean valid) {
        getOkButton().setEnabled(valid);
    }

    protected HashMap<String, Long> getSuggestions() {
        return mSuggestions;
    }

    protected void setSuggestions(HashMap<String, Long> suggestions) {
        mSuggestions = suggestions;
        updateSuggestionAdapter();
    }

    protected abstract void suggestionSelected(final String name, final long id);

    protected abstract void newNameEntered(final String name);

    protected abstract int valueNameId();

    protected abstract int valueCompletionHintId();

    protected abstract void onOk();

    /**
     * Layout id of the content of the dialog.
     *
     * @return Implementors should return null if no layout resource is in use.
     */
    protected abstract Integer contentLayoutId();

    protected LinearLayout getContentArea() {
        return Utils.View.getTypeSafeView(getView(), R.id.add_value_content_area, LinearLayout.class);
    }

    private TextView getNameTitle() {
        return Utils.View.getTypeSafeView(getView(), R.id.name_title, TextView.class);
    }

    private AutoCompleteTextView getNameTextView() {
        return Utils.View.getTypeSafeView(getView(), R.id.autocomplete_name, AutoCompleteTextView.class);
    }

    private Button getOkButton() {
        return Utils.View.getTypeSafeView(getView(), R.id.ok_button, Button.class);
    }

    private void updateSuggestionAdapter() {
        if (mSuggestions == null) {
            return;
        }

        if (getNameTextView() == null) {
            return;
        }

        Set<String> set = mSuggestions.keySet();
        String[] suggestions = new String[set.size()];
        set.toArray(suggestions);

        View view = Utils.checkNotNull(getView());
        Context context = Utils.checkNotNull(view.getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                suggestions);

        getNameTextView().setAdapter(adapter);
    }
}
