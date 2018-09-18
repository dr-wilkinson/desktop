package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;
import io.github.drw.rules.timing.Instant;

/**
 * An {@link Adventure} has been loaded. For now the adventure merely contains
 * the current {@link Instant}.
 *
 * @author dr-wilkinson
 */
public class LoadAdventure extends AbstractEvent {

    /**
     * Constructs a new LoadAdventure {@link Event}.
     *
     * @param source The source of the Event.
     * @param object The object to be handled by the {@link Listener}.
     */
    public LoadAdventure(Object source, Object object) {
        super(null, source, object);
    }

}
