package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class EventListAdapter extends OptimizedListAdapter<EventListAdapter.Tag, View, Event.Value> {
    protected static class Tag {
        public ImageView eventIcon;
        public TextView title;
        public TextView time;
    }

    private DateTimeFormatter mDateTimeFormatter;

    public EventListAdapter(Activity context, int layoutId) {
        super(context, layoutId, EventListAdapter.Tag.class, View.class);
        init(context);
    }

    public EventListAdapter(Activity context, int layoutId, List<Event.Value> data) {
        super(context, layoutId, data, EventListAdapter.Tag.class, View.class);
        init(context);
    }

    private void init(Context context) {
        mDateTimeFormatter = DateTimeFormat.forPattern(context.getString(R.string.event_date_time_format));
    }

    /**
     * Get a tag for a view. A tag should contain references to every view modified when new data is
     * used.
     *
     * @param view The view requiring a tag.
     * @return A new tag for the view.
     */
    @Override
    protected Tag getTagFor(final View view) {
        return new Tag() {
            {
                eventIcon = Utils.View.getTypeSafeView(view, R.id.eventIcon, ImageView.class);
                title = Utils.View.getTypeSafeView(view, R.id.eventTitle, TextView.class);
                time = Utils.View.getTypeSafeView(view, R.id.eventTime, TextView.class);
            }
        };
    }

    /**
     * Use a tag to populate the features of a view. The tag will have been returned by
     * {@link #getTagFor(View view)}.
     *
     * @param tag  The tag to use with the view.
     * @param data The data to populate the tag's views with.
     */
    @Override
    protected void populateTag(Tag tag, Event.Value data) {
        int iconId;
        switch (Event.Type.valueOf(data.typeId)) {
            case MEAL:
                iconId = R.drawable.ic_food;
                break;
            case MEDICATION:
                iconId = R.mipmap.ic_medication;
                break;
            case HEALTH_SCORE:
                iconId = R.mipmap.ic_health;
                break;
            case NOTE:
                iconId = R.mipmap.ic_note;
                break;
            default:
                iconId = R.drawable.ic_launcher;
                break;
        }


        tag.eventIcon.setImageResource(iconId);

        tag.title.setText(data.name);
        tag.time.setText(
                com.robwilliamson.healthyesther.db.Utils.Time.toString(
                        data.when,
                        mDateTimeFormatter));
    }
}
