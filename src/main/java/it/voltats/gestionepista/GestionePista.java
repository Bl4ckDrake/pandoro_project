package it.voltats.gestionepista;
import it.voltats.gestionepista.presentation.MenuManagement;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import it.voltats.gestionepista.ui.views.JFXCalendar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionePista extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // CALENDAR
        // Event manager which contains all the events
        CalendarEventManager eventManager = new CalendarEventManager();

        // Calendar view
        JFXCalendar calendar = new JFXCalendar(eventManager);

        /* Start window */
        Scene scene = new Scene(calendar, 1080, 720);
        stage.setTitle("Gestione Pista - Home");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String []args) {
        boolean isCli = false;
        for(String arg: args) {
            if(arg.equals("--cli")) {
                isCli = true;
                break;
            }
        }

        if(isCli)
            new MenuManagement().execute();
        else
            launch();
    }
}

