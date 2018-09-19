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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main entry point for the application.
 *
 * @author dr wilkinson <dr-wilkinson@users.noreply.github.com>
 */
public class DesktopApplication extends Application implements Listener {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Eventbus.getInstance().add(this);
        Eventbus.toggleReporting();
        Parent root = FXMLLoader.load(getClass().getResource("DesktopView.fxml"));
        Scene scene = new Scene(root);
        this.stage = stage;
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // TODO : Automatic save of open campaigns
    }

    @Override
    public Event handle(Event event) {
        if (event instanceof ApplicationEvent) {
            event.addListener(this);
            if (event.getType().equals(ApplicationEvent.Type.Quit)) {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                event.consume(this);
            }
        }
        return event;
    }

}
