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
package io.github.drw.desktop.campaigns;

import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.events.AdventureEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class AdventureTreeItem<String> extends AbstractTreeItem {

    public AdventureTreeItem(String title) {
        setValue(title);
    }

    @Override
    public ContextMenu getContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new AdventureEvent(AdventureEvent.Type.Save, AdventureTreeItem.this, null));
            }
        });
        contextMenu.getItems().add(saveMenuItem);
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        contextMenu.getItems().add(separatorMenuItem);
        return contextMenu;
    }

}
