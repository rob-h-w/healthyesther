package com.robwilliamson.healthyesther.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.robwilliamson.db.Utils;
import com.robwilliamson.healthyesther.BuildConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;

public enum TimingManager {
    INSTANCE;

    private static final int MILLISECONDS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    //private static final int PERIOD = 2 * MINUTES_IN_HOUR * SECONDS_IN_MINUTE * MILLISECONDS_IN_SECOND;
    private static final int PERIOD = 10000;
    private static final int MULTIPLE_NOTIFICATION_THRESHOLD_S = 60;
    private static final int FUTURE_OFFSET_S = 5;

    private static final String PREFERENCES_NAME =
            "com.robwilliamson.healthyesther.reminder.TimingManager";
    private static final String NEXT_REMINDER = "next_reminder";
    private static final String PREVIOUS_REMINDER = "previous_reminder";
    private static final String REQUEST_CODE = "request_code";

    private static final String LOG_TAG = TimingManager.class.getSimpleName();

    private Context mContext;
    private Long mLastRequest = null;
    private int mPeriodMillis = PERIOD;

    public void setPeriodMillis(int periodMillis) {
        mPeriodMillis = periodMillis;
    }

    public void resetPeriodMillis() {
        setPeriodMillis(PERIOD);
    }

    public void applicationCreated(Context context) {
        log("applicationCreated");
        setContext(context);
        if (getNextReminderTime() == null) {
            start(context);
        }
    }

    public void alarmElapsed(Context context, Intent intent) {
        log("alarmElapsed");
        setContext(context);
        if (mLastRequest == null || intent.getLongExtra(REQUEST_CODE, -1) == mLastRequest) {
            start(context);
        }
    }

    public boolean notifyNow(Context context) {
        setContext(context);
        DateTime lastReminder = getPreviousReminderTime();

        if (lastReminder == null) {
            return isNowWithinNotificationRange();
        }

        return isNowWithinNotificationRange() &&
                lastReminder.plusSeconds(MULTIPLE_NOTIFICATION_THRESHOLD_S).isAfterNow() &&
                lastReminder.minusSeconds(MULTIPLE_NOTIFICATION_THRESHOLD_S).isAfterNow();
    }

    public void notificationMade(Context context) {
        log("notificationMade");
        setContext(context);
        setPreviousReminderTimeNow();
        setNextReminderTime(null);
        start(context);
        log("last reminder time is now " + getPreviousReminderTime());
    }

    public void start(Context context) {
        log("start");
        setContext(context);

        if (getNextReminderTime() == null) {
            DateTime nextDateTime = getNextTimeToSet();
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            long millisUntil = nextDateTime.getMillis() - Utils.Time.localNow().getMillis();

            log("setting new notification in " + millisUntil + "ms, expected to trigger at " + nextDateTime);
            DateTime prev = getPreviousReminderTime();
            log("previous notification was " + (prev == null ? "not sent." : " sent at " + prev + "."));

            alarmManager.set(AlarmManager.ELAPSED_REALTIME, millisUntil, getOperation());

            setNextReminderTime(nextDateTime);
        }
    }

    private SharedPreferences getPreferences() {
        return getContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private DateTime getNextTimeToSet() {
        if (!isNowWithinNotificationRange()) {
            return getRangeEarliestTomorrow(Utils.Time.localNow());
        }

        if (getPreviousReminderTime() == null) {
            return Utils.Time.localNow().plusSeconds(1);
        }

        DateTime proposedNextTime = Utils.Time.localNow().plusMillis(mPeriodMillis);
        DateTime nextThreshold = Utils.Time.localNow().plusSeconds(MULTIPLE_NOTIFICATION_THRESHOLD_S);

        if (proposedNextTime.isBefore(nextThreshold)) {
            proposedNextTime = nextThreshold;
        }

        if (isWithinNotificationRange(proposedNextTime)) {
            return proposedNextTime;
        }

        return getRangeLatest(proposedNextTime);
    }

    private PendingIntent getOperation() {
        Intent intent = new Intent(getContext(), ReminderIntentService.class);

        if (mLastRequest == null) {
            mLastRequest = 0L;
        } else {
            mLastRequest++;
        }

        intent.putExtra(REQUEST_CODE, mLastRequest);

        return PendingIntent.getService(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean isNowWithinNotificationRange() {
        return isWithinNotificationRange(Utils.Time.localNow());
    }

    private boolean isWithinNotificationRange(DateTime time) {
        DateTime earliest = getRangeEarliest(time);
        DateTime earliestTomorrow = getRangeEarliestTomorrow(time);
        DateTime latest = getRangeLatest(time);

        return ((time.isEqual(earliest) || time.isAfter(earliest)) &&
                (time.isEqual(latest) || time.isBefore(latest))) ||
                (time.isAfter(latest) && time.isAfter(earliestTomorrow));
    }

    private boolean isWithinRange(DateTime time, DateTime rangeCentre, Duration sigma) {
        DateTime min = rangeCentre.minus(sigma);
        DateTime max = rangeCentre.plus(sigma);
        return (time.isEqual(min) || time.isAfter(min)) &&
                (time.isEqual(max) || time.isBefore(max));
    }

    private DateTime getRangeEarliest(DateTime time) {
        return time.withHourOfDay(8).withMinuteOfHour(0);
    }

    private DateTime getRangeEarliestTomorrow(DateTime time) {
        return getRangeEarliest(time).withDurationAdded(Duration.standardDays(1), 1);
    }

    private DateTime getRangeLatest(DateTime time) {
        return time.withHourOfDay(22).withMinuteOfHour(0);
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    private DateTime getNextReminderTime() {
        return getTime(NEXT_REMINDER);
    }

    private void setNextReminderTime(DateTime time) {
        setTime(NEXT_REMINDER, time);
    }

    private DateTime getPreviousReminderTime() {
        return getTime(PREVIOUS_REMINDER);
    }

    private void setPreviousReminderTimeNow() {
        setTime(PREVIOUS_REMINDER, Utils.Time.localNow());
    }

    private DateTime getTime(String key) {
        String timeString = getPreferences().getString(key, null);

        if (timeString == null) {
            return null;
        }

        return Utils.Time.fromLocalString(timeString);
    }

    private void setTime(String key, DateTime time) {
        if (time == null) {
            getPreferences().edit().remove(key).commit();
        } else {
            getPreferences().edit().putString(key, Utils.Time.toLocalString(time)).commit();
        }
    }

    private static void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, message);
        }
    }
}
