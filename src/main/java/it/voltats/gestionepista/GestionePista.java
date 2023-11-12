package it.voltats.gestionepista;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import it.voltats.gestionepista.ui.views.JFXCalendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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

        Scene scene = new Scene(calendar, 1080, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String []args){
        //new MenuManagement().execute();
        launch();
    }

    private void debugInfo(String resourceName) {
        System.err.println("Working Directory = {}" + System.getProperty("user.dir"));
        System.err.println("Resources for {}" + resourceName);
        try {
            Enumeration<URL> urls = ClassLoader.getSystemClassLoader().getResources(resourceName);
            while (urls.hasMoreElements()) {
                System.err.println(urls.nextElement());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

