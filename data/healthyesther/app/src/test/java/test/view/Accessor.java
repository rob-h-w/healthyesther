package test.view;

import android.app.Activity;
import android.widget.TextView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;

abstract class Accessor {
    @Nonnull
    private final ActivityTestContext mContext;

    Accessor(@Nonnull ActivityTestContext context) {
        mContext = context;
    }

    @Nullable
    public String getText(int id) {
        return ((TextView) getActivity().findViewById(id)).getText().toString();
    }

    public void setText(@Nullable String text, int id) {
        ((TextView) getActivity().findViewById(id)).setText(text);
    }

    @Nonnull
    protected Activity getActivity() {
        return mContext.getActivity();
    }
}
