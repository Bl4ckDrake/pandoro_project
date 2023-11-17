package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.voltats.gestionepista.GestionePista;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class CSVDialog extends JFXDialog {

    private CalendarEventManager manager;
    private CSVDialogController csvDialogController;

    public CSVDialog(StackPane stackPane) {
        JFXDialogLayout content = new JFXDialogLayout();

        URL location = GestionePista.class.getClassLoader().getResource("fxml/CSVDialog.fxml");

        FXMLLoader loader = new FXMLLoader();
        loader.setClassLoader(getClass().getClassLoader());
        loader.setLocation(location);

        VBox bodyPane = null;
        try {
            bodyPane = loader.load();
            csvDialogController = loader.getController();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        content.setBody(bodyPane);

        JFXButton canselButton = new JFXButton("cancel");
        canselButton.getStyleClass().add("removeButton");
        canselButton.setOnAction(e -> {
            this.close();
        });

        HBox actionButtonPane = new HBox(15);

        actionButtonPane.setPadding(new Insets(20, 0, 0, 0));
        actionButtonPane.getChildren().addAll(canselButton);

        content.setActions(actionButtonPane);

        setDialogContainer(stackPane);
        setContent(content);

        URL url = getClass().getResource("/style/DialogStyle.css");
        getStylesheets().add(url.toExternalForm());
    }

    public void clear() {
        csvDialogController.clear();
    }

}
