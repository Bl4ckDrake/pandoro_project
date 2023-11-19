package it.voltats.gestionepista.ui.views;

import java.io.IOException;

import it.voltats.gestionepista.ui.controllers.EventDetailPaneController;
import it.voltats.gestionepista.ui.dialog.DialogHandler;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import org.controlsfx.glyphfont.FontAwesome;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class EventPane extends VBox {

	private Label fromLabel = new Label();
	private Label titleLabel = new Label();
	private Label messageLabel = new Label();
	private VBox priorityBox = new VBox();

	private CalendarEventManager eventManager;

	private VBox parentPane;
	private CalendarEvent event;

	private StackPane eventStackPane;

	private CalendarDayView calendarDayView;
	private Label priorityLabel;

	public EventPane(CalendarEvent event) {
		this.event = event;

		fromLabel.setVisible(false);

		String title = event.getTitle();
		String text = event.getDescription();

		VBox.setMargin(this, new Insets(0, 40, 0, 20));
		setPadding(new Insets(15, 10, 15, 10));

		setSpacing(10);
		setPrefHeight(100);

		HBox.setMargin(this, new Insets(3, 0, 0, 0));

		priorityBox = new VBox();
		priorityBox.setAlignment(Pos.CENTER);
		priorityBox.setMinSize(125, 25);
		priorityBox.setPrefSize(125, 25);
		priorityBox.setMaxSize(125, 25);

		HBox topPane = new HBox(priorityBox);
		topPane.setSpacing(7);

		getStylesheets().add(this.getClass().getResource("/style/EventPaneStyle.css")
				.toExternalForm());

		priorityLabel = new Label("");
		priorityLabel.setId("priorityLabel");

		priorityBox.getChildren().add(priorityLabel);

		refreshStyle();

		titleLabel.setText("Title : " + title);
		messageLabel.setText("Description : " + text);

		messageLabel.maxWidthProperty().bind(this.widthProperty());

		titleLabel.setPadding(new Insets(0, 0, 0, 10));
		messageLabel.setPadding(new Insets(0, 0, 0, 10));
		fromLabel.setPadding(new Insets(0, 0, 0, 10));

		titleLabel.setId("titleLabel");
		messageLabel.setId("messageLabel");
		fromLabel.setId("fromLabel");

		JFXButton editButton = new JFXButton();

		editButton
				.setGraphic(new FontAwesome().create(FontAwesome.Glyph.PENCIL).size(18));

		editButton.setOnAction(e -> editEvent());

		editButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		HBox buttonPane = new HBox(editButton);
		buttonPane.setSpacing(10);
		buttonPane.setAlignment(Pos.CENTER);

		HBox emptyPane = new HBox();
		HBox.setHgrow(emptyPane, Priority.ALWAYS);

		HBox topBox = new HBox();
		topBox.setSpacing(10);
		topBox.getChildren().addAll(topPane, emptyPane, buttonPane);

		getChildren().addAll(topBox, titleLabel, messageLabel);

	}

	private void refreshStyle() {
		titleLabel.setText("Title : " + event.getTitle());
		messageLabel.setText("Description : " + event.getDescription());

		int priority = event.getPriority();
		if (priority == CalendarEvent.HOLIDAY) {
			priorityLabel.setText("Holiday");
			priorityBox.setId("optionalPriorityBox");
			setId("optionalEvent");
		} else if (priority == CalendarEvent.CONFIRMED) {
			priorityLabel.setText("Confirmed");
			priorityBox.setId("standardPriorityBox");
			setId("standardEvent");
		} else if (priority == CalendarEvent.PENDING) {
			priorityLabel.setText("Pending");
			priorityBox.setId("importantPriorityBox");
			setId("importantEvent");
		} else {
			priorityLabel.setText("Expired / Cancelled");
			priorityBox.setId("urgentPriorityBox");
			setId("urgentEvent");
		}
	}


	private void editEvent() {

		if (event.getPriority() == CalendarEvent.HOLIDAY) {
			DialogHandler.showErrorDialog("Holiday event cannot be edited");
			return;
		}

		JFXDialog dialog = new JFXDialog();
		JFXDialogLayout content = new JFXDialogLayout();

		FXMLLoader eventDetailsPaneLoader = new FXMLLoader(this.getClass()
				.getResource("/fxml/EventDetailPane.fxml"));

		try {
			VBox mainPane = eventDetailsPaneLoader.load();
			content.setBody(mainPane);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		EventDetailPaneController controller = eventDetailsPaneLoader.getController();
		controller.loadEvent(event);
		controller.setDialog(dialog);

		dialog.getStylesheets().add(this.getClass()
				.getResource("/style/DialogStyle.css").toExternalForm());

		dialog.setDialogContainer(this.eventStackPane);
		dialog.setContent(content);

		dialog.show();

		dialog.setOnDialogClosed(e -> {
			if (controller.hasUpdates()) {
				if (calendarDayView != null) {
					calendarDayView.refreshCalendar(calendarDayView.currectDate);
				} else {
					refreshStyle();
					JFXCalendar.todayButton.fire();
				}
			}
		});
	}

	public void setEventParentPane(VBox parentPane) {
		this.parentPane = parentPane;
	}

	public void setEventManager(CalendarEventManager manager) {
		this.eventManager = manager;
	}

	public void setStackPaneRoot(StackPane eventStackPane) {
		this.eventStackPane = eventStackPane;
	}

	void setCalendarView(CalendarDayView calendarDayView) {
		this.calendarDayView = calendarDayView;
	}
}