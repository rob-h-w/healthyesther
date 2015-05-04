package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;

import org.joda.time.DateTime;

import static com.robwilliamson.healthyesther.Utils.checkNotNull;

public abstract class GetEventsAfterDateTimeQuery extends GetSomeValuesQuery {
    private final DateTime mFrom;

    public GetEventsAfterDateTimeQuery(DateTime from) {
        mFrom = checkNotNull(from);
    }

    @Override
    public String getTableName() {
        return Event.TABLE_NAME;
    }

    @Override
    public String getWhereSelection() {
        return getOrderColumn() +
                " >= datetime(\"" +
                Utils.Time.toDatabaseString(mFrom) +
                "\")";
    }

    @Override
    public String getOrderColumn() {
        return Event.WHEN;
    }

    @Override
    public String getOrder() {
        return "DESC";
    }

    @Override
    public String[] getResultColumns() {
        return null;
    }
}
