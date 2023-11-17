package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import javafx.fxml.FXML;

public class EventDetailPaneController {

	@FXML
	private JFXButton updateEventButton;

	@FXML
	private JFXButton closeDialogButton;


	@FXML
	private JFXButton confirmedEventButton;

	@FXML
	private JFXButton pendingEventButton;

	@FXML
	private JFXButton cancelledEventButton;


	private JFXDialog dialog;

	private CalendarEvent event;

	private int selectedPriority = -1;

	private boolean hasChanged = false;

	private BookingBusiness bookingBusiness = new BookingBusiness();

	@FXML
	private void initialize() {

		closeDialogButton.setOnAction(e -> {
			dialog.close();
		});


		confirmedEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.CONFIRMED);
		});

		pendingEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.PENDING);
		});

		cancelledEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.CANCELLED);
		});

		updateEventButton.setOnAction(e -> {
			// update event information
			Booking booking = bookingBusiness.findById(event.getId());

			switch (selectedPriority) {
				case CalendarEvent.CONFIRMED:
					booking.setStatus(BookingStatus.CONFIRMED);
					break;
				case CalendarEvent.PENDING:
					booking.setStatus(BookingStatus.PENDING);
					break;
				case CalendarEvent.CANCELLED:
					booking.setStatus(BookingStatus.STORED);
					break;
			}

			bookingBusiness.update(booking);

			event.setPriority(selectedPriority);

			hasChanged = true;
			dialog.close();
		});
	}

	public void loadEvent(CalendarEvent event) {
		this.event = event;

		selectPriority(event.getPriority());
	}

	private void clearPriorityOptions() {
		confirmedEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
		pendingEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
		cancelledEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
	}

	private void selectPriority(int priority) {
		clearPriorityOptions();

		if (priority == CalendarEvent.CONFIRMED) {
			confirmedEventButton.setStyle(
					"-fx-background-color: #81C457; -fx-background-radius:15px; ");
		} else if (priority == CalendarEvent.PENDING) {
			pendingEventButton.setStyle(
					"-fx-background-color: #F7C531; -fx-background-radius:15px; ");
		} else if(priority == CalendarEvent.CANCELLED) {
			cancelledEventButton.setStyle(
					"-fx-background-color: #E85569; -fx-background-radius:15px; ");
		}
		this.selectedPriority = priority;
	}

	public void setDialog(JFXDialog dialog) {
		this.dialog = dialog;
	}

	public boolean hasUpdates() {
		return hasChanged;
	}
}