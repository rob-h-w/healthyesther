package com.robwilliamson.healthyesther.db.definition;


public abstract class EventModification extends Modification {
    private Event.Modification mEventModification;

    public Event.Modification getEventModification() {
        return mEventModification;
    }

    public void setEventModification(Event.Modification eventModification) {
        mEventModification = eventModification;
    }
}
