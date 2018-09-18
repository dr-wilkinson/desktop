package io.github.drw.desktop.timing;

import io.github.drw.desktop.eventbus.Event;
import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.Listener;
import io.github.drw.desktop.eventbus.events.ClockUpdate;
import io.github.drw.desktop.eventbus.events.CombatRound;
import io.github.drw.desktop.eventbus.events.CustomInstant;
import io.github.drw.desktop.eventbus.events.LoadAdventure;
import io.github.drw.rules.adventures.Adventure;
import io.github.drw.rules.timing.Instant;
import io.github.drw.rules.timing.Period;

/**
 * A Clock keeps track of the current {@link Instant}. It listens for
 * {@link Event}s that denote the passage of a {@link Period} of {@link Time}
 * and when they are received, it updates the current {@link Instant} by the
 * {@link Period} the {@link Event} carries. It then broadcasts the fact that
 * the current {@link Instant} has incremented and the new current
 * {@link Instant} itself to all objects that need to update themselves based on
 * the passage of {@link Time}.
 *
 * @author dr-wilkinson
 */
public class Clock implements Listener {

    private static Clock INSTANCE;
    private static Instant now;

    private Clock() {
        now = new Instant(1, 1, 0, 0, 0);
    }

    /**
     * Returns a static instance of this Clock.
     *
     * @return The Clock.
     */
    public static Clock instance() {
        if (INSTANCE == null) {
            return INSTANCE = new Clock();
        } else {
            return INSTANCE;
        }
    }

    @Override
    public Event handle(Event event) {
        if (event.getSource() == this) {
            return event;
        } else {
            if (event instanceof LoadAdventure) {
                if (event.getObject() instanceof Adventure) {
                    now = ((Adventure) event.getObject()).getInstant();
                    event.consume();
                    Eventbus.fire(new ClockUpdate(this, now.clone()));
                    return event;
                }
            }
            if (event instanceof CombatRound) {
                if (event.getObject() instanceof Instant) {
                    System.out.println("beep");
                    Instant instantA = now;
                    Instant instantB = ((Instant) event.getObject()).clone();
                    Period period = Period.add(instantA, instantB);
                    now = new Instant(period.getYears(), period.getDays(), period.getHours(), period.getMinutes(), period.getSeconds());
                    Eventbus.fire(new ClockUpdate(this, now.clone()));
                    return event;
                }
            }
            if (event instanceof CustomInstant) {
                if (event.getObject() instanceof Instant) {
                    Instant instantA = now;
                    Instant instantB = ((Instant) event.getObject()).clone();
                    Period period = Period.add(instantA, instantB);
                    now = new Instant(period.getYears(), period.getDays(), period.getHours(), period.getMinutes(), period.getSeconds());
                    Eventbus.fire(new ClockUpdate(this, now.clone()));
                    return event;
                }
            }
        }
        return event;
    }

    public static Instant now() {
        return now.clone();
    }

    public static void reset() {
        now = new Instant(1, 1, 0, 0, 0);
    }

}
