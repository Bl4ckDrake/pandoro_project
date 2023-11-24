package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import com.jfoenix.controls.JFXTextField;
import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class EventDetailPaneController {

	@FXML
	private JFXButton updateEventButton;

	@FXML
	private JFXButton closeDialogButton;


	@FXML
	private JFXButton confirmedEventButton;

	@FXML
	private JFXButton cancelledEventButton;

	@FXML
	private HBox applyDiscountBox;

	@FXML
	private Label initPriceLabel;

	@FXML
	private JFXTextField discountTextField;

	@FXML
	private Label finalPriceLabel;


	private JFXDialog dialog;

	private CalendarEvent event;

	private int selectedPriority = -1;

	private boolean hasChanged = false;

	private BookingBusiness bookingBusiness = new BookingBusiness();

	private double finalPrice;

	@FXML
	private void initialize() {

		closeDialogButton.setOnAction(e -> {
			dialog.close();
		});


		confirmedEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.CONFIRMED);
			applyDiscountBox.setVisible(true);
			Booking booking = bookingBusiness.findById(event.getId());
			initPriceLabel.setText(booking.getPrice() + "$");

			finalPrice = booking.getPrice();
			finalPriceLabel.setText(finalPrice + "$");

			discountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    discountTextField.setText(newValue.replaceAll("[^\\d]", ""));

                }

				if(!discountTextField.getText().isBlank()) {
					finalPrice = booking.getPrice() - booking.getPrice()/100*Integer.parseInt(discountTextField.getText());
					finalPriceLabel.setText(finalPrice + "$");
				}
				else {
					finalPrice = booking.getPrice();
					finalPriceLabel.setText(finalPrice + "$");
				}
			});
		});

		cancelledEventButton.setOnAction(e -> {
			selectPriority(CalendarEvent.CANCELLED);
			applyDiscountBox.setVisible(false);
		});

		updateEventButton.setOnAction(e -> {
			// update event information
			Booking booking = bookingBusiness.findById(event.getId());

			switch (selectedPriority) {
				case CalendarEvent.CONFIRMED:
					if (bookingBusiness.getIsForewarned(booking.getStartDate())) {
						booking.setStatus(BookingStatus.CONFIRMED);
						booking.setPrice(finalPrice);
					}
					else
						selectedPriority = CalendarEvent.CANCELLED;
					break;
				case CalendarEvent.CANCELLED:
					if (bookingBusiness.getIsForewarned(booking.getStartDate()))
						booking.setStatus(BookingStatus.STORED);
					else
						selectedPriority = CalendarEvent.CONFIRMED;
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
		cancelledEventButton
				.setStyle("-fx-background-color: #BDC6CC; -fx-background-radius:15px; ");
	}

	private void selectPriority(int priority) {
		clearPriorityOptions();

		if (priority == CalendarEvent.CONFIRMED) {
			confirmedEventButton.setStyle(
					"-fx-background-color: #81C457; -fx-background-radius:15px; ");
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