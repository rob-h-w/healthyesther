/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.app.Activity;
import android.view.View;
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
        return getText(getActivity().findViewById(id).getRootView(), id);
    }

    @Nullable
    public String getText(View view, int id) {
        return ((TextView) view.findViewById(id)).getText().toString();
    }

    public void setText(@Nullable String text, int id) {
        ((TextView) getActivity().findViewById(id)).setText(text);
    }

    @Nonnull
    protected Activity getActivity() {
        return mContext.getActivity();
    }

    @Nonnull
    protected ActivityTestContext getContext() {
        return mContext;
    }
}
