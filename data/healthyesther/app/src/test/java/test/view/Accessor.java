package test.view;

import android.app.Activity;
import android.widget.TextView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

abstract class Accessor {
    @Nonnull
    private final Activity mActivity;

    Accessor(@Nonnull Activity activity) {
        mActivity = activity;
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
        return mActivity;
    }
}
