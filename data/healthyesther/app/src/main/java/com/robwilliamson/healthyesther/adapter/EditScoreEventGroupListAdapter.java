package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.List;

public class EditScoreEventGroupListAdapter extends OptimizedListAdapter<EditScoreEventGroupListAdapter.Tag, View, Pair<HealthScore.Score, Integer>> {

    protected static class Tag {
        public TextView title;
        public TextView minLabel;
        public TextView maxLabel;
        public RatingBar ratingBar;
        public LinearLayout layout;
    }

    public EditScoreEventGroupListAdapter(Activity context) {
        super(context, R.layout.fragment_edit_score_event);
    }

    public EditScoreEventGroupListAdapter(Activity context, List<Pair<HealthScore.Score, Integer>> list) {
        super(context, R.layout.fragment_edit_score_event, list);
    }

    @Override
    protected Tag getTagFor(final View view) {
        return new Tag() {
            {
                title = Utils.View.getTypeSafeView(view, R.id.score_name_title);
                minLabel = Utils.View.getTypeSafeView(view, R.id.score_minimum_label);
                maxLabel = Utils.View.getTypeSafeView(view, R.id.score_maximum_label);
                ratingBar = Utils.View.getTypeSafeView(view, R.id.score_bar);
                layout = Utils.View.getTypeSafeView(view, R.id.edit_score_layout);
            }
        };
    }

    @Override
    protected void populateTag(Tag tag, Pair<HealthScore.Score, Integer> data) {
        tag.title.setText(data.first.name);
        tag.minLabel.setText(data.first.minLabel);
        tag.maxLabel.setText(data.first.maxLabel);
        tag.ratingBar.setMax(HealthScore.MAX);
        tag.ratingBar.setRating(data.second);
    }
}
