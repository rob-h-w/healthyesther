package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.R.string;
import android.support.annotation.NonNull;

public class ConfirmationDialogFragment extends FixedDialogFragment {
    private static final String TITLE = ConfirmationDialogFragment.class.getCanonicalName() + ".title";
    private static final String MESSAGE = ConfirmationDialogFragment.class.getCanonicalName() + ".message";

    public interface Observer {
        void onDialogOk(ConfirmationDialogFragment dialogFragment);
    }

    public static ConfirmationDialogFragment create(int title, int message) {
        ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(TITLE, title);
        arguments.putInt(MESSAGE, message);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getInt(TITLE)).
                setMessage(getArguments().getInt(MESSAGE)).
                setPositiveButton(string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object parentObject = getParentFragment();
                        if (parentObject == null) {
                            parentObject = getActivity();
                        }

                        if (parentObject instanceof Observer) {
                            ((Observer) parentObject).onDialogOk(ConfirmationDialogFragment.this);
                        } else {
                            throw new ClassCastException(
                                    "The object " + parentObject + " must implement " + Observer.class.getCanonicalName());
                        }
                    }
                }).setNegativeButton(string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        return builder.create();
    }
}
