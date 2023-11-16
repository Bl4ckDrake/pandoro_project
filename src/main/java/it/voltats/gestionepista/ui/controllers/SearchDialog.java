package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.voltats.gestionepista.GestionePista;
import it.voltats.gestionepista.ui.controllers.AddEventDialogController;
import it.voltats.gestionepista.ui.dialog.DialogHandler;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class SearchDialog extends JFXDialog {

    private CalendarEventManager manager;
    private AddSearchDialogController searchDialogController;

    public SearchDialog(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();

        URL location = GestionePista.class.getClassLoader().getResource("fxml/AddSearchDialog.fxml");

        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);

        VBox bodyPane = null;
        try {
            bodyPane = loader.load();
            searchDialogController = loader.getController();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        content.setBody(bodyPane);

        setDialogContainer(stackPane);
        setContent(content);

        URL url = getClass().getResource("/style/DialogStyle.css");
        getStylesheets().add(url.toExternalForm());
    }

    private void addEvent(CalendarEvent event) {
        manager.addEvent(event);
    }

    public void clear() {
        searchDialogController.clear();
    }

    public void setCalendarEventManager(CalendarEventManager manager) {
        this.manager = manager;
    }
}
