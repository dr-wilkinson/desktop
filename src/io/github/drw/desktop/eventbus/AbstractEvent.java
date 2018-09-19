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
import java.util.Collections;
import java.util.List;

/**
 * Implements the @{link Event} interface.
 *
 * @author dr-wilkinson
 */
public class AbstractEvent implements Event {

    private final Object type;
    private boolean consumed = false;
    private final Object object;
    private final Object source;

    private List<Listener> listeners = new ArrayList<>();
    private Object consumer;

    /**
     * Constructs an abstract event.
     *
     * @param type The type of this event.
     * @param source The source of this event.
     * @param object The object associated with this event.
     */
    public AbstractEvent(Object type, Object source, Object object) {
        this.type = type;
        this.source = source;
        this.object = object;
    }

    @Override
    public Object getType() {
        return type;
    }

    @Override
    public Object getObject() {
        return object;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }

    @Override
    public void consume(Object consumer) {
        this.consumer = consumer;
        consumed = true;
    }

    @Override
    public Object getConsumer() {
        return consumer;
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public List<Listener> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

}
