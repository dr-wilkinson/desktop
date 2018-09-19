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

import java.util.List;

/**
 * The public contract for all events.
 *
 * @author dr-wilkinson
 */
public interface Event {

    /**
     * Returns the specific Type of this Event.
     *
     * @return The event type.
     */
    public Object getType();

    /**
     * Returns the data associated with this Event.
     *
     * @return The event data.
     */
    public Object getObject();

    /**
     * Returns the source of this Event.
     *
     * @return The source of this event.
     */
    public Object getSource();

    /**
     * Has this Event been consumed?
     *
     * @return True if consumed, otherwise false.
     */
    public boolean isConsumed();

    /**
     * Consume this Event.
     */
    public void consume(Object consumer);

    /**
     * Returns the {@link Object} that consumed this Event.
     *
     * @return The consumer.
     */
    public Object getConsumer();

    /**
     * Adds any {@link Listener} that is listening for this type of Event.
     *
     * @param listener The listening object.
     */
    public void addListener(Listener listener);

    /**
     * Returns a {@link List} of all objects that have listened to this Event.
     *
     * @return All objects that have listened to this Event.
     */
    public List<Listener> getListeners();

}
