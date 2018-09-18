package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;

/**
 * This {@link Event} indicates that a name has been generated.
 *
 * @author dr-wilkinson
 */
public class GenerateName extends AbstractEvent {

    /**
     * Constructs a new GenerateName {@link Event}.
     *
     * @param source The source of the Event.
     * @param object The object to be handled by the {@link Listener}.
     */
    public GenerateName(Object source, Object object) {
        super(null, source, object);
    }

}
