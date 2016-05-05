/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;

public class EditEventFragmentAccessor extends Accessor {
    public EditEventFragmentAccessor(@Nonnull ActivityTestContext context) {
        super(context);
    }

    @Nullable
    public String getName() {
        return getText(R.id.edit_event_name);
    }

    public void setName(@Nullable String name) {
        setText(name, R.id.edit_event_name);
    }
}
