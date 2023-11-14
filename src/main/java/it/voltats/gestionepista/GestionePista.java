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


        /* Add fixed holidays */
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

        /* Add pasqua and pasquetta */
        LocalDate now = LocalDate.now();
        Calendar pasqua = holidaysUtils.getEasterForYear(now.getYear());
        Calendar pasquetta = holidaysUtils.getPasquettaForYear(now.getYear());

        int pasquaMonth = pasqua.get(Calendar.MONTH) + 2;
        if (pasquaMonth > 12) {
            pasquaMonth -= 12;
        }

        // TODO: make action better + fix
        LocalDate pasquaDate = LocalDate.of(pasqua.get(Calendar.YEAR), pasquaMonth, pasqua.get(Calendar.DAY_OF_MONTH));
        if(eventManager.getEventsOn(pasquaDate).isEmpty()) {
            eventManager.addEvent(new CalendarEvent(-1, "Pasqua", 3, "Chiuso", CalendarEvent.IMPORTANT, CalendarEvent.ONE_TIME_EVENT, pasquaDate, "", -1, null));
        }


        int pasquettaMonth = pasquetta.get(Calendar.MONTH) + 1;
        if (pasquettaMonth > 12) {
            pasquettaMonth -= 12;
        }

        LocalDate pasquettaDate = LocalDate.of(pasquetta.get(Calendar.YEAR), pasquettaMonth, pasquetta.get(Calendar.DAY_OF_MONTH));
        if(eventManager.getEventsOn(pasquaDate).isEmpty()) {
            eventManager.addEvent(new CalendarEvent(-1, "Pasquetta", 3, "Chiuso", CalendarEvent.IMPORTANT, CalendarEvent.ONE_TIME_EVENT, pasquettaDate, "", -1, null));
        }


        /* Start window */
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

