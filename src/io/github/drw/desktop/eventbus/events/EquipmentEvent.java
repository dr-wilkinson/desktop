/*
 * PersonalEvent.java
 *
 * Copyright (c) 2018 dr wilkinson <dr-wilkinson@users.noreply.github.com>.
 *
 * This file is part of Traveller.
 *
 * Traveller is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Traveller is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Traveller.  If not, see <http ://www.gnu.org/licenses/>.
 */
package io.github.drw.desktop.eventbus.events;

import io.github.drw.desktop.eventbus.AbstractEvent;
import io.github.drw.desktop.eventbus.Event;

/**
 * This class of {@link Event} is fired by objects that manipulate
 * {@link Skills} objects.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class EquipmentEvent extends AbstractEvent implements Event {

    public enum Type {

        Create,
        Return,
        Update,
        Delete,
        Error;

    }

    public EquipmentEvent(final Type type, final Object source, final Object object) {
        super(type, source, object);
    }

}
