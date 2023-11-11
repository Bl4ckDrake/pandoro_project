package it.voltats.gestionepista.ui.controllers;

import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import it.voltats.gestionepista.GestionePista;
import it.voltats.gestionepista.ui.dialog.DialogHandler;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EventDialog extends JFXDialog {

	private CalendarEventManager manager;
	private AddEventDialogController eventDialogController;

	public EventDialog(StackPane stackPane) {
		JFXDialogLayout content = new JFXDialogLayout();

		/*URL location = GestionePista.class.getClassLoader().getResource("fxml/AddEventDialog.fxml");
		System.out.println(location);
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setClassLoader(getClass().getClassLoader());
		fxmlLoader.setLocation(location);*/

		//FXMLLoader loader = new FXMLLoader(
		//		this.getClass().getResource("/views/fxml/AddEventDialog.fxml"));
		URL location = GestionePista.class.getClassLoader().getResource("AddEventDialog.fxml");

		FXMLLoader loader = new FXMLLoader();
		loader.setClassLoader(getClass().getClassLoader());
		loader.setLocation(location);

		VBox bodyPane = null;
		try {
			bodyPane = loader.load();
			eventDialogController = loader.getController();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		content.setBody(bodyPane);

		JFXButton canselButton = new JFXButton("cancel");
		canselButton.getStyleClass().add("removeButton");
		canselButton.setOnAction(e -> {
			this.close();
		});

		JFXButton addButton = new JFXButton("Add");
		addButton.getStyleClass().add("addButton");
		HBox actionButtonPane = new HBox(15);
		addButton.setOnAction(e -> {
			CalendarEvent event = eventDialogController.getEvent();

			if (event == null) {
				DialogHandler.showErrorDialog(
						"Event canceled. There were not enough data for its creation");
			} else {
				addEvent(event);
			}

			this.close();
		});

		actionButtonPane.setPadding(new Insets(20, 0, 0, 0));
		actionButtonPane.getChildren().addAll(canselButton, addButton);

		content.setActions(actionButtonPane);

		setDialogContainer(stackPane);
		setContent(content);



		//getStylesheets().add("/home/bl4ckdrake/IdeaProjects/pandoro_project/src/main/resources/style/DialogStyle.css");

	}

	private void addEvent(CalendarEvent event) {
		manager.addEvent(event);
	}

	public void clear() {
		eventDialogController.clear();
	}

	public void setCalendarEventManager(CalendarEventManager manager) {
		this.manager = manager;
	}
}
