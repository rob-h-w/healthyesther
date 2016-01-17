package test.view;

import com.robwilliamson.healthyesther.R;

import test.ActivityTestContext;

public class EditNoteEventFragmentAccessor extends Accessor {
    public EditNoteEventFragmentAccessor(ActivityTestContext context) {
        super(context);
    }

    public String getName() {
        return getText(R.id.note_name);
    }

    public void setName(String name) {
        setText(name, R.id.note_name);
    }

    public String getDetail() {
        return getText(R.id.note_content);
    }

    public void setDetail(String detail) {
        setText(detail, R.id.note_content);
    }
}
