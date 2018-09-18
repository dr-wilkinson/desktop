package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;

/**
 * A custom {@link Period} event.
 *
 * @author dr-wilkinson
 */
public class CustomInstant extends AbstractEvent {

    public CustomInstant(Object source, Object object) {
        super(null, source, object);
    }

}
