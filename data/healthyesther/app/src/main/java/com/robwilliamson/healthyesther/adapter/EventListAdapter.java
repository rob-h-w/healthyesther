/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class EventListAdapter extends OptimizedListAdapter<EventListAdapter.Tag, View, EventTable.Row> {
    private volatile static DateTimeFormatter sDateTimeFormatter;

    public EventListAdapter(Activity context, int layoutId) {
        super(context, layoutId, EventListAdapter.Tag.class, View.class);
        init(context);
    }

    private synchronized void init(Context context) {
        if (sDateTimeFormatter == null) {
            sDateTimeFormatter = DateTimeFormatter.ofPattern(context.getString(R.string.event_date_time_format));
        }
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
     * {@link OptimizedListAdapter<>#getTagFor(V view)}.
     *
     * @param tag  The tag to use with the view.
     * @param data The data to populate the tag's views with.
     */
    @Override
    protected void populateTag(Tag tag, EventTable.Row data) {
        int iconId;
        switch (EventTypeTable.valueOf(data.getTypeId())) {
            case MEAL:
                iconId = R.drawable.ic_food;
                break;
            case MEDICATION:
                iconId = R.mipmap.ic_medication;
                break;
            case HEALTH:
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

        tag.title.setText(data.getName());
        tag.time.setText(
                com.robwilliamson.healthyesther.db.Utils.Time.toString(
                        data.getWhen().as(ZonedDateTime.class),
                        sDateTimeFormatter));
    }

    protected static class Tag {
        @SuppressWarnings("WeakerAccess")
        public ImageView eventIcon;
        public TextView title;
        public TextView time;
    }
}
