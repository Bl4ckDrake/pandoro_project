package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.entity.model.Gender;
import it.voltats.gestionepista.db.impl.UserRepoImpl;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import org.controlsfx.control.RangeSlider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class AddEventDialogController {

	@FXML
	private ComboBox userSelector;

	@FXML
	private HBox addUserBox;

	// Booking data fields
	@FXML
	private DatePicker startDateField;
	@FXML
	private DatePicker endDateField;

	@FXML
	private JFXTextField startTimeField;
	@FXML
	private JFXTextField endTimeField;

	// User fields
	@FXML
	private JFXTextField nameField;
	@FXML
	private JFXTextField surnameField;
	@FXML
	private JFXTextField cfField;
	@FXML
	private JFXTextField emailField;
	@FXML
	private JFXTextField phoneNumberField;

	private ToggleGroup genderGroup;
	@FXML
	private JFXRadioButton maleRadioButton;
	@FXML
	private JFXRadioButton femaleRadioButton;

	@FXML
	private DatePicker birthDatePicker;


	private boolean showUserFields;
	private int selectedUserId;

	private UserBusiness userBusiness = new UserBusiness();
	private BookingBusiness bookingBusiness = new BookingBusiness();

	@FXML
	public void initialize() {

		// Set radio buttons in a group
		genderGroup = new ToggleGroup();
		maleRadioButton.setToggleGroup(genderGroup);
		femaleRadioButton.setToggleGroup(genderGroup);
		maleRadioButton.setUserData(Gender.M);
		femaleRadioButton.setUserData(Gender.F);

		// Set box to add user invisible
		addUserBox.setVisible(false);


		FontAwesomeIconView plusIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
		plusIcon.setFill(Paint.valueOf("#767676"));
		plusIcon.setSize("24");

		FontAwesomeIconView closeIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
		closeIcon.setFill(Paint.valueOf("#767676"));
		closeIcon.setSize("24");


		List<User> userList = userBusiness.findAll();

		ObservableList<String> options =
				FXCollections.observableArrayList();

		for (User user: userList) {
			options.add("User: " + user.getName() + " " + user.getSurname() + " " + user.getCf());
		}

		options.add("Add new user");

		userSelector.setItems(options);

		userSelector.valueProperty().addListener((observableValue, o, t1) -> {
            if(t1.equals("Add new user")) {
                showUserFields = true;
                addUserBox.setVisible(true);
				selectedUserId = -1;
            }
            else {
                showUserFields = false;
                addUserBox.setVisible(false);
				selectedUserId = userList.get(options.indexOf(t1)).getId();
            }
        });

	}

	private User getUser() {
		if(nameField.getText().isBlank() || surnameField.getText().isBlank() || cfField.getText().isBlank() || emailField.getText().isBlank() || phoneNumberField.getText().isBlank() || genderGroup.getSelectedToggle() == null || birthDatePicker.getValue() == null) {
			return null;
		}

		Gender gender;
		if(genderGroup.getSelectedToggle().getUserData().equals(Gender.M))
			gender = Gender.M;
		else
			gender = Gender.F;

		return new User(nameField.getText(), surnameField.getText(), gender, Date.from(birthDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), cfField.getText(), emailField.getText(), phoneNumberField.getText());
	}



	public void clear() {
		startDateField.setValue(null);
		endDateField.setValue(null);
		startTimeField.setText("");
		endTimeField.setText("");
		nameField.setText("");
		surnameField.setText("");
		cfField.setText("");
		emailField.setText("");
		phoneNumberField.setText("");
		birthDatePicker.setValue(null);
	}

	public CalendarEvent getEvent() {

		// user
		int userId = -1;
		User user;

		if(selectedUserId == -1) {
			 user = getUser();

			if(user == null) {
				return null;
			}

			userId = userBusiness.insert(user);

			selectedUserId = userId;
		}
		else {
			userId = selectedUserId;
			user = userBusiness.findById(userId);
		}

        // booking
		LocalDate startLocalDate = startDateField.getValue();
		LocalDate endLocalDate = endDateField.getValue();
		Date startDate;
		Date endDate;

		try {
			startDate = new SimpleDateFormat("dd/MM/yyyy HH:ss").parse(startLocalDate.getDayOfMonth() + "/" + startLocalDate.getMonthValue() + "/" + startLocalDate.getYear() + " " + startTimeField.getText());
			endDate = new SimpleDateFormat("dd/MM/yyyy HH:ss").parse(endLocalDate.getDayOfMonth() + "/" + endLocalDate.getMonthValue() + "/" + endLocalDate.getYear() + " " + endTimeField.getText());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

		int bookingId = bookingBusiness.insert(new Booking(userId, startDate, endDate, BookingStatus.PENDING, -1));

		if(bookingId == -1) {
			return null;
		}

		// event
		CalendarEvent event = null;

		if (startDateField.getValue() != null && endDateField.getValue() != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			event = new CalendarEvent(user.getName() + " " + user.getSurname(), CalendarEvent.PENDING, "Start time: " + simpleDateFormat.format(startDate) + ", End time: " + simpleDateFormat.format(endDate));
			event.setId(bookingId);
			event.setType(CalendarEvent.ONE_TIME_EVENT);
			event.setDate(startDateField.getValue());
		}

		return event;
	}

}