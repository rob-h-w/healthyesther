package com.robwilliamson.healthyesther.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractAddNamedDialog extends Dialog {
    private HashMap<String, Long> mSuggestions = null;

    public AbstractAddNamedDialog(Context context) {
        super(context);
        initialize();
    }

    public AbstractAddNamedDialog(Context context, int theme) {
        super(context, theme);
        initialize();
    }

    protected AbstractAddNamedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            doQuery(getQuery());
        }
    }

    /**
     * Called when the dialog is starting.
     */
    @Override
    protected void onStart() {
        super.onStart();

        getNameTitle().setText(getContext().getText(valueNameId()));

        AutoCompleteTextView name = getNameTextView();
        name.setCompletionHint(getContext().getText(valueCompletionHintId()));
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                AbstractAddNamedDialog.this.newNameEntered(text);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suggestion = (String)parent.getAdapter().getItem(position);
                AbstractAddNamedDialog.this.suggestionSelected(suggestion, mSuggestions.get(suggestion));
            }
        });
        getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk();
                dismiss();
            }
        });

        if (contentLayoutId() != null) {
            View.inflate(getContext(), contentLayoutId(), getContentArea());
        }

        updateSuggestionAdapter();
    }

    protected String getName() {
        return getNameTextView().getText().toString();
    }

    protected void setName(String name) {
        getNameTextView().setText(name);
    }

    protected void setSuggestions(HashMap<String, Long> suggestions) {
        mSuggestions = suggestions;
        updateSuggestionAdapter();
    }

    protected void setValid(boolean valid) {
        getOkButton().setEnabled(valid);
    }

    protected HashMap<String, Long> getSuggestions() {
        return mSuggestions;
    }

    protected abstract Query getQuery();

    protected abstract void doQuery(final Query query);

    protected abstract void queryComplete(final Cursor result);

    protected abstract void queryFailed(final Throwable error);

    protected abstract void suggestionSelected(final String name, final long id);

    protected abstract void newNameEntered(final String name);

    protected abstract int valueNameId();

    protected abstract int valueCompletionHintId();

    protected abstract void onOk();

    /**
     * Layout id of the content of the dialog.
     * @return Implementors should return null if no layout resource is in use.
     */
    protected abstract Integer contentLayoutId();

    protected LinearLayout getContentArea() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.add_value_content_area);
    }

    private void initialize() {
        setContentView(R.layout.dialog_add_value);
    }

    private TextView getNameTitle() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.name_title);
    }

    private AutoCompleteTextView getNameTextView() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.autocomplete_name);
    }

    private Button getOkButton() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.ok_button);
    }

    private void updateSuggestionAdapter() {
        if (mSuggestions == null) {
            return;
        }

        if (getNameTextView() == null) {
            return;
        }

        Set<String> set = mSuggestions.keySet();
        String [] suggestions = new String[set.size()];
        set.toArray(suggestions);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions);

        getNameTextView().setAdapter(adapter);
    }
}
