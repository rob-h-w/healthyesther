/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.widget.SeekBar;
import android.widget.Switch;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;

public class EditScoreFragmentAccessor extends Accessor {
    public EditScoreFragmentAccessor(@Nonnull ActivityTestContext context) {
        super(context);
    }

    public void setName(@Nullable String scoreName) {
        setText(scoreName, R.id.autocomplete_name);
    }

    public void setAskPeriodically(boolean ask) {
        ((Switch) getActivity().findViewById(R.id.random_query)).setChecked(ask);
    }

    public void setMax(String max) {
        setText(max, R.id.max_label);
    }

    public void setMin(String min) {
        setText(min, R.id.min_label);
    }

    public int getBestScore() {
        return ((SeekBar) getActivity().findViewById(R.id.best_value)).getProgress();
    }

    public void setBestScore(int bestScore) {
        ((SeekBar) getActivity().findViewById(R.id.best_value)).setProgress(bestScore);
    }
}
