package com.ruskonert.GamblKing.engine.event;

import java.util.HashMap;
import java.util.Map;

public abstract class Event
{
    private Map<?, ?> eventObjectValue = new HashMap<>();
    public Map<?, ?> getEventObjectValue() { return this.eventObjectValue; }

    public void start()
    {
        EventController.invokeEvent(this);
    }
}
