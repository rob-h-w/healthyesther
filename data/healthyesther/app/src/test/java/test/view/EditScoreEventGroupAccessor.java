/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.view.View;
import android.view.ViewGroup;

import com.robwilliamson.healthyesther.R;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;

public class EditScoreEventGroupAccessor extends Accessor {
    public EditScoreEventGroupAccessor(@Nonnull ActivityTestContext context) {
        super(context);
    }

    @Nullable
    public EditScoreEventAccessor getScore(@Nonnull String scoreName) {
        List<EditScoreEventAccessor> scores = getScores();

        for (EditScoreEventAccessor score : scores) {
            if (scoreName.equals(score.getName())) {
                return score;
            }
        }

        return null;
    }

    @Nullable
    public EditScoreEventAccessor getScore(int index) {
        List<EditScoreEventAccessor> scores = getScores();
        if (index < 0 || index >= scores.size()) {
            return null;
        }

        return scores.get(index);
    }

    public int getScoreCount() {
        return getScores().size();
    }

    @Nonnull
    private List<EditScoreEventAccessor> getScores() {
        ViewGroup eventGroup = getEventGroup();
        final int count = eventGroup == null ? 0 : eventGroup.getChildCount();
        ArrayList<EditScoreEventAccessor> scores = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            View view = eventGroup.getChildAt(i);

            if (view instanceof ViewGroup && view.getId() == R.id.edit_score_layout) {
                scores.add(new EditScoreEventAccessor((ViewGroup) view, getContext()));
            }
        }

        return scores;
    }

    @Nullable
    private ViewGroup getEventGroup() {
        return (ViewGroup) getActivity().findViewById(R.id.edit_score_group_layout);
    }
}
