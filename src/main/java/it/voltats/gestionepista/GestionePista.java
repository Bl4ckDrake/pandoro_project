package it.voltats.gestionepista;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import it.voltats.gestionepista.ui.views.JFXCalendar;
import it.voltats.gestionepista.util.ItalianHolidaysUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Enumeration;

public class GestionePista extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //debugInfo("home-view.fxml");

        URL location = getClass().getClassLoader().getResource("fxml/home-view.fxml");
        System.out.println(location);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setClassLoader(getClass().getClassLoader());
        fxmlLoader.setLocation(location);
        VBox vBox = fxmlLoader.load();


        // CALENDAR
        // Event manager which contains all the events
        CalendarEventManager eventManager = new CalendarEventManager();

        // Calendar view
        JFXCalendar calendar = new JFXCalendar(eventManager);

        //eventManager.addEvent(new CalendarEvent(-1, "Natale", 3, "Natale", CalendarEvent.IMPORTANT, CalendarEvent.PER_YEAR, null, "", -1, LocalDate.of(2023, 12, 25)));

        ItalianHolidaysUtils holidaysUtils = ItalianHolidaysUtils.getInstance();

        final String [] FESTIVITY_NAME = {
                "Capodanno",
                "Epifania",
                "Festa della Liberazione",
                "Festa del Lavoro",
                "Festa della Repubblica",
                "Ferragosto",
                "San Cassiano (Patrono Imola)",
                "Tutti i Santi",
                "Immacolata Concezione",
                "Natale",
                "Santo Stefano"
        };

        int i=0;
        for (Calendar holidayCalendar: holidaysUtils.fixedHolidays) {
            // Calculate month
            int month = holidayCalendar.get(Calendar.MONTH) + 2;
            if(month> 12) {
                month -= 12;
            }

            // Get day from calendar
            int day = holidayCalendar.get(Calendar.DAY_OF_MONTH);

            // Add event
            eventManager.addEvent(new CalendarEvent(-1, FESTIVITY_NAME[i], 3, "Chiuso", CalendarEvent.IMPORTANT, CalendarEvent.PER_YEAR, null, "", -1, LocalDate.of(2022, month, day)));
            i++;
        }

        Scene scene = new Scene(calendar, 1080, 720);
        stage.setTitle("Gestione Pista - Home");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String []args){
        //new MenuManagement().execute();
        launch();
    }
}

