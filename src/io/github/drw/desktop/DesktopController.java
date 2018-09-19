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

import io.github.drw.desktop.eventbus.Event;
import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.Listener;
import io.github.drw.desktop.eventbus.events.ApplicationEvent;
import io.github.drw.desktop.eventbus.events.CampaignEvent;
import io.github.drw.desktop.eventbus.events.StatusEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * The DesktopController for the {@link Desktop} {@link View} FXML.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class DesktopController implements Initializable, Listener {

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
    private TextField campaignTitleTextField;
    @FXML
    private AnchorPane sessionAnchorPane;
    @FXML
    private TextArea chatDisplayTextArea;
    @FXML
    private TextArea chatInputTextArea;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Eventbus.getInstance().add(this);
        initMenuItems();
    }

    private void initMenuItems() {
        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new CampaignEvent(CampaignEvent.Type.Open, DesktopController.this, null));
            }
        });
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new CampaignEvent(CampaignEvent.Type.Save, DesktopController.this, null));
            }
        });
        quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Eventbus.fire(new ApplicationEvent(ApplicationEvent.Type.Quit, DesktopController.this, null));
            }
        });
    }

    @Override
    public Event handle(Event event) {
        if (event instanceof StatusEvent) {
            event.addListener(this);
            if (event.getType().equals(StatusEvent.Type.Error)) {
                System.out.println("TODO : Change the text colour to orange denoting an error.");
                String message = (String) event.getObject();
                statusLabel.textProperty().setValue(message);
                event.consume(this);
            }
            if (event.getType().equals(StatusEvent.Type.Update)) {
                System.out.println("TODO : Change the text colour to green denoting a message.");
                String message = (String) event.getObject();
                statusLabel.textProperty().setValue(message);
                event.consume(this);
            }
        }
        return event;
    }

}
