package com.robwilliamson.healthyesther.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.App;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractAddNamedDialog extends Dialog {
    HashMap<String, Long> mSuggestions = null;

    public AbstractAddNamedDialog(Context context) {
        super(context);
    }

    public AbstractAddNamedDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AbstractAddNamedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

        getNameTitle().setText(getContext().getText(R.string.health_score));
        getNameTextView().setCompletionHint(getContext().getText(R.string.descriptive_name_for_the_score));

        updateSuggestionAdapter();
    }

    protected void setSuggestions(HashMap<String, Long> suggestions) {
        mSuggestions = suggestions;
        updateSuggestionAdapter();
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

    private TextView getNameTitle() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.name_title);
    }

    private AutoCompleteTextView getNameTextView() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.autocomplete_name);
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
