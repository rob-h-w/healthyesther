package com.robwilliamson.db.definition;


public abstract class EventModification extends Modification {
    private Event.Modification mEventModification;

    public void setEventModification(Event.Modification eventModification) {
        mEventModification = eventModification;
    }

    public Event.Modification getEventModification() {
        return mEventModification;
    }
}
