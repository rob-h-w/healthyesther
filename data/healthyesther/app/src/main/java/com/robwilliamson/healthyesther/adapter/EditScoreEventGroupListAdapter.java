package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.HealthScore;

import java.util.List;

public class EditScoreEventGroupListAdapter extends OptimizedListAdapter<EditScoreEventGroupListAdapter.Tag, View, Pair<HealthScore.Value, Integer>> {

    public EditScoreEventGroupListAdapter(Activity context) {
        super(context, R.layout.fragment_edit_score_event, EditScoreEventGroupListAdapter.Tag.class, View.class);
    }

    public EditScoreEventGroupListAdapter(Activity context, List<Pair<HealthScore.Value, Integer>> list) {
        super(context, R.layout.fragment_edit_score_event, list, EditScoreEventGroupListAdapter.Tag.class, View.class);
    }

    @Override
    protected Tag getTagFor(final View view) {
        return new Tag() {
            {
                title = Utils.View.getTypeSafeView(view, R.id.score_name_title, TextView.class);
                minLabel = Utils.View.getTypeSafeView(view, R.id.score_minimum_label, TextView.class);
                maxLabel = Utils.View.getTypeSafeView(view, R.id.score_maximum_label, TextView.class);
                ratingBar = Utils.View.getTypeSafeView(view, R.id.score_bar, RatingBar.class);
                layout = Utils.View.getTypeSafeView(view, R.id.edit_score_layout, LinearLayout.class);
            }
        };
    }

    @Override
    protected void populateTag(Tag tag, Pair<HealthScore.Value, Integer> data) {
        tag.title.setText(data.first.name);
        tag.minLabel.setText(data.first.minLabel);
        tag.maxLabel.setText(data.first.maxLabel);
        tag.ratingBar.setMax(HealthScore.MAX);
        tag.ratingBar.setRating(data.second);
    }

    protected static class Tag {
        public TextView title;
        public TextView minLabel;
        public TextView maxLabel;
        public RatingBar ratingBar;
        public LinearLayout layout;
    }
}
