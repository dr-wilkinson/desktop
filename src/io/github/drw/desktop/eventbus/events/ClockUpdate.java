package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;

/**
 * A clock update {@link Event}. ClockUpdate events indicate to any Listeners
 * that the Clock has updated by a period of Time. The Time period that has
 * passed can be accessed from the ClockUpdate object.
 *
 * @author dr-wilkinson
 */
public class ClockUpdate extends AbstractEvent {

    /**
     * Constructs a new ClockUpdate {@link Event}.
     *
     * @param source The source of the Event.
     * @param object The object to be handled by the {@link Listener}.
     */
    public ClockUpdate(Object source, Object object) {
        super(null, source, object);
    }

}
