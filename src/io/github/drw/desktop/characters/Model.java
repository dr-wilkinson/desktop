/*
 * Model.java
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
package io.github.drw.desktop.characters;

import io.github.drw.rules.adventures.Adventure;

/**
 * Encapsulates the data that both the {@link Controller} and {@link DataAccess}
 * use.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class Model {

    private Adventure adventure;

    /**
     * Returns the {@link Adventure}.
     *
     * @return The Adventure.
     */
    Adventure getAdventure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Sets the {@link Adventure}.
     *
     * @param adventure The Adventure.
     */
    void setAdventure(Adventure adventure) {
        this.adventure = adventure;
    }

}
