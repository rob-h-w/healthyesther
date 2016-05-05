/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nullable;

import test.ActivityTestContext;

public class EditNoteEventFragmentAccessor extends Accessor {
    public EditNoteEventFragmentAccessor(ActivityTestContext context) {
        super(context);
    }

    @Nullable
    public String getName() {
        return getText(R.id.note_name);
    }

    public void setName(@Nullable String name) {
        setText(name, R.id.note_name);
    }

    @Nullable
    public String getDetail() {
        return getText(R.id.note_content);
    }

    public void setDetail(@Nullable String detail) {
        setText(detail, R.id.note_content);
    }

    @Nullable
    public AutoCompleteTextView getNameView() {
        return (android.widget.AutoCompleteTextView) getActivity().findViewById(R.id.note_name);
    }
}
