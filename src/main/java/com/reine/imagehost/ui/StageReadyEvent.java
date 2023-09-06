package com.reine.imagehost.ui;

import org.springframework.context.ApplicationEvent;

public class StageReadyEvent extends ApplicationEvent {
    public StageReadyEvent(EventProperty property) {
        super(property);
    }

    public EventProperty getProperty() {
        return (EventProperty) getSource();
    }
}