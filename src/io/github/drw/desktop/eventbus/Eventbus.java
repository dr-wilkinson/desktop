/*
 * Copyright (C) 2018 dr wilkinson <dr-wilkinson@users.noreply.github.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.drw.desktop.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * An eventbus that passes fired events on to any registered {@link Listener}s.
 *
 * @author dr-wilkinson
 */
public class Eventbus {

    private static Eventbus eventbus;
    private static List<Listener> listeners;

    private Eventbus() {
        listeners = new ArrayList<>();
    }

    /**
     * Returns one and only one instance of this eventbus.
     *
     * @return one eventbus instance.
     */
    public static Eventbus getInstance() {
        if (eventbus == null) {
            eventbus = new Eventbus();
        }
        return eventbus;
    }

    /**
     * Fires the supplied {@link Event} off to all {@link Listener}s.
     *
     * @param event The Event to fire.
     */
    public static void fire(Event event) {
        if (event != null) {
            for (Listener listener : listeners) {
                event = listener.handle(event);
                if (event.isConsumed()) {
                    break;
                }
            }
            event = null;
        } else {
            throw new IllegalArgumentException("Cannot supply a null event!");
        }
    }

    /**
     * Adds a Listener.
     *
     * @param listener The Listener to add.
     */
    public void add(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        } else {
            throw new IllegalArgumentException("Cannot add a null listener!");
        }
    }

    /**
     * Removes a Listener.
     *
     * @param listener The listener to remove.
     */
    public void remove(Listener listener) {
        if (listener != null) {
            if (!listeners.remove(listener)) {
                throw new IllegalArgumentException("Eventbus does not contain listener!");
            }
        } else {
            throw new IllegalArgumentException("Cannot remove a null listener!");
        }
    }

}
