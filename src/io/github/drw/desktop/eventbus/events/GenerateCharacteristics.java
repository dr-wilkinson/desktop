package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;

/**
 * An {@link Event} specifically relating to {@link Characteristics} objects.
 *
 * @author dr-wilkinson
 */
public class GenerateCharacteristics extends AbstractEvent {

    /**
     * Constructs a CharacteristsEvent.
     *
     * @param source The source of this event.
     * @param object The object to be handled by the {@link Listener}.
     */
    public GenerateCharacteristics(Object source, Object object) {
        super(null, source, object);
    }

}
