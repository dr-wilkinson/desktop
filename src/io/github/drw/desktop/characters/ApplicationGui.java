package io.github.drw.desktop.characters;

import io.github.drw.desktop.eventbus.Event;
import io.github.drw.desktop.eventbus.Eventbus;
import io.github.drw.desktop.eventbus.Listener;
import io.github.drw.desktop.eventbus.events.CloseApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The main entry point for the application.
 *
 * @author dr-wilkinson
 */
public class ApplicationGui extends Application implements Listener {

    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Eventbus.getInstance().add(this);
        Parent root = FXMLLoader.load(getClass().getResource("View.fxml"));
        Scene scene = new Scene(root);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.show();
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
        if (event instanceof CloseApplication) {
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            event.consume();
        }
        return event;
    }

}
