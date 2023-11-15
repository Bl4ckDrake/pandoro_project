package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import org.controlsfx.control.RangeSlider;

public class AddEventDialogController {


	@FXML
	private DatePicker dateField;

	@FXML
	private ComboBox userSelector;

	@FXML
	private JFXButton addUserButton;

	@FXML
	private RangeSlider timeRangePicker;

	private int eventType;

	@FXML
	public void initialize() {
		addPriorityButtonListeners();


		FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
		icon.setFill(Paint.valueOf("#767676"));
		icon.setSize("24");

		addUserButton.getStyleClass().add("circle_buttom");
		addUserButton.setGraphic(icon);

		addUserButton.setOnAction(event -> {
			// TODO: add user
		});

		// TODO: user selector
		ObservableList<String> options =
				FXCollections.observableArrayList(
						"Option 1",
						"Option 2",
						"Option 3"
				);

		userSelector.setItems(options);



		// TODO: time range picker
		timeRangePicker.setShowTickLabels(true);
		timeRangePicker.setShowTickMarks(false);
		timeRangePicker.setBlockIncrement(1);

		timeRangePicker.adjustHighValue(480);
		timeRangePicker.adjustLowValue(60);
		timeRangePicker.setMax(1440);
	}



	private void addPriorityButtonListeners() {
		// Event Button Listeners
	}

	private void cleanSelection() {
		eventType = -1;
	}

	public int getEventType() {
		return eventType;
	}

	public void clear() {

		cleanSelection();

		dateField.setValue(null);
	}

	public CalendarEvent getEvent() {

		CalendarEvent event = null;

		if (dateField.getValue() != null) {
			// TODO: title
			event = new CalendarEvent("title", eventType, "eventNote1.getText()");
			event.setType(CalendarEvent.ONE_TIME_EVENT);
			event.setDate(dateField.getValue());
		}

		return event;
	}




}