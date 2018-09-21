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

import javafx.scene.Node;
import javafx.scene.control.TreeCell;

/**
 * A custom {@link TreeCell}.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class CustomTreeCell extends TreeCell<Object> {

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getDisplayText(item));
            setGraphic(getDisplayGraphic(item));
            setContextMenu(((AbstractTreeItem) getTreeItem()).getContextMenu());
        }
    }

    private String getDisplayText(Object item) {
        if (item instanceof CampaignFile) {
            return ((CampaignFile) item).getNameTitle();
        }
        return null;
    }

    private Node getDisplayGraphic(Object item) {
        if (item instanceof CampaignFile) {
            return getTreeItem().getGraphic();
        }
        return null;
    }

}
