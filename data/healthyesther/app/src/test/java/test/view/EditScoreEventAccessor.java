/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.view.ViewGroup;
import android.widget.RatingBar;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;

import test.ActivityTestContext;

public class EditScoreEventAccessor extends Accessor {
    private final ViewGroup mScore;

    EditScoreEventAccessor(@Nonnull ViewGroup score, @Nonnull ActivityTestContext context) {
        super(context);
        mScore = score;
    }

    public String getName() {
        return getText(R.id.score_name_title);
    }

    public String getMinimumLabel() {
        return getText(R.id.score_minimum_label);
    }

    public String getMaximumLabel() {
        return getText(R.id.score_maximum_label);
    }

    public float getRating() {
        RatingBar bar = (RatingBar) mScore.findViewById(R.id.score_bar);
        return bar.getRating();
    }

    public void setRating(float rating) {
        RatingBar bar = (RatingBar) mScore.findViewById(R.id.score_bar);
        bar.setRating(rating);
    }
}
