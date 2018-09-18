package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;
import io.github.drw.rules.timing.Instant;

/**
 * Encapsulates a round of combat. The CombatRound includes a period of Time of
 * pre-determined and fixed duration as per the rules: 15 seconds.
 *
 * @author dr-wilkinson
 */
public class CombatRound extends AbstractEvent {

    /**
     * Constructs a new CombatRound {@link Event}.
     *
     * @param source The source of the Event.
     * @param object The object to be handled by the {@link Listener}.
     */
    public CombatRound(Object source, Object object) {
        super(null, source, new Instant(0, 0, 0, 0, 15));
    }

}
