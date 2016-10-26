package com.populace.berrycollege.managers;

import com.parse.ParseClassName;
import com.populace.berrycollege.models.GTCObject;

@ParseClassName("academic")
public class Academic extends GTCObject {
    final static String ICO_FIELD_EVENT_CALANDER = "event_calender";

    public String getEventCal()
    {
        return getString(ICO_FIELD_EVENT_CALANDER);
    }

    public void setEventCal(String n) {
        put(ICO_FIELD_EVENT_CALANDER, n);
    }
}
