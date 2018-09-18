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
package io.github.drw.desktop;

import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.events.CloseApplication;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 * The Controller for the {@link Desktop} {@link View} FXML.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class Controller implements Initializable {

    @FXML
    private MenuItem newMenuItem;
    @FXML
    private MenuItem openMenuItem;
    @FXML
    private Menu openRecentMenu;
    @FXML
    private MenuItem saveMenuItem;
    @FXML
    private MenuItem saveAsMenuItem;
    @FXML
    private MenuItem revertMenuItem;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem preferencesMenuItem;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem editorMenuItem;
    @FXML
    private MenuItem sessionMenuItem;
    @FXML
    private AnchorPane editorAnchorPane;
    @FXML
    private TreeView<?> campaignsTreeView;
    @FXML
    private TextField campaignTitleTextField;
    @FXML
    private AnchorPane sessionAnchorPane;
    @FXML
    private TextArea chatDisplayTextArea;
    @FXML
    private TextArea chatInputTextArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMenuItems();
    }

    private void initMenuItems() {
        quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new CloseApplication(this, null));
            }
        });
    }

}
