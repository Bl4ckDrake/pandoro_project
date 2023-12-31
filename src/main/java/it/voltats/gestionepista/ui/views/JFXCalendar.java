package it.voltats.gestionepista.ui.views;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.ui.controllers.CSVDialog;
import it.voltats.gestionepista.ui.controllers.SearchDialog;
import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import it.voltats.gestionepista.util.ItalianHolidaysUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class JFXCalendar extends StackPane {

	private final int DAY = 0;
	private final int WEEK = 1;
	private final int MONTH = 2;

	private JFXButton menuButton;

	private LocalDate selectedDate;

	private CalendarWeekView calendarWeekView;
	private CalendarDayView calendarDayView;

	private BorderPane mainPane;
	private int currentView;
	private NavigationCalendar navigationCalendar;
	private Label dateLabel;

	private CalendarEventManager eventManager;

	private StringProperty navigationDateProperty;

	private VBox leftPane;

	private BooleanProperty confirmedFilterProperty;
	private BooleanProperty pendingFilterProperty;
	private BooleanProperty holidayFilterProperty;
	private BooleanProperty cancelledFilterProperty;
	private BooleanProperty completedFilterProperty;
	private ToggleButton dayButton;
	private ToggleButton weekButton;
	private ToggleButton monthButton;

	private JFXButton searchButton;
	private JFXButton csvButton;

	private CalendarMonthView calendarMonthView;

	public static JFXButton todayButton;

	public JFXCalendar(CalendarEventManager eventManager) {

		initBookingsAndHolidays(eventManager);

		URL url = getClass().getResource("/style/CalendarScheduler.css");
		getStylesheets().add(url.toExternalForm());

		this.eventManager = eventManager;
		selectedDate = LocalDate.now();

		mainPane = new BorderPane();
		getChildren().add(mainPane);

		initLeftPane();
		initMainPane();
		initToolBar();
	}

	private void initBookingsAndHolidays(CalendarEventManager eventManager) {
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
			eventManager.addEvent(new CalendarEvent(-1, FESTIVITY_NAME[i], CalendarEvent.HOLIDAY, "Chiuso per festività", 3, CalendarEvent.PER_YEAR, null, "", -1, LocalDate.of(2022, month, day)));
			i++;
		}

		/* Add pasqua and pasquetta */
		LocalDate now = LocalDate.now();
		Calendar pasqua = holidaysUtils.getEasterForYear(now.getYear());
		Calendar pasquetta = holidaysUtils.getPasquettaForYear(now.getYear());

		int pasquaMonth = pasqua.get(Calendar.MONTH) + 1;
		if (pasquaMonth > 12) {
			pasquaMonth -= 12;
		}

		LocalDate pasquaDate = LocalDate.of(pasqua.get(Calendar.YEAR), pasquaMonth, pasqua.get(Calendar.DAY_OF_MONTH));
		if(eventManager.getEventsOn(pasquaDate).isEmpty()) {
			CalendarEvent event = new CalendarEvent("Pasqua", CalendarEvent.HOLIDAY, "Chiuso per festività");
			event.setId(-1);
			event.setType(CalendarEvent.ONE_TIME_EVENT);
			event.setDate(pasquaDate);
			eventManager.addEvent(event);
		}


		int pasquettaMonth = pasquetta.get(Calendar.MONTH) + 1;
		if (pasquettaMonth > 12) {
			pasquettaMonth -= 12;
		}

		LocalDate pasquettaDate = LocalDate.of(pasquetta.get(Calendar.YEAR), pasquettaMonth, pasquetta.get(Calendar.DAY_OF_MONTH));
		if(eventManager.getEventsOn(pasquaDate).isEmpty()) {
			CalendarEvent event = new CalendarEvent("Pasquetta", CalendarEvent.HOLIDAY, "Chiuso per festività");
			event.setId(-1);
			event.setType(CalendarEvent.ONE_TIME_EVENT);
			event.setDate(pasquettaDate);
			eventManager.addEvent(event);
		}

		// Add bookings
		BookingBusiness bookingBusiness = new BookingBusiness();
		UserBusiness userBusiness = new UserBusiness();
		bookingBusiness.setOldBookingsExpired();
		bookingBusiness.findAll().forEach(booking -> {
			CalendarEvent event;
			User user = userBusiness.findById(booking.getUserId());

			int priority = -1;
			switch (booking.getStatus()) {
				case CONFIRMED -> priority = CalendarEvent.CONFIRMED;
				case PENDING -> priority = CalendarEvent.PENDING;
				case STORED -> priority = CalendarEvent.CANCELLED;
			}

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			event = new CalendarEvent(user.getName() + " " + user.getSurname() + " (Booking ID: " + booking.getId() + ")", priority, "Start time: " + simpleDateFormat.format(booking.getStartDate()) + ", End time: " + simpleDateFormat.format(booking.getEndDate()));
			event.setId(booking.getId());
			event.setType(CalendarEvent.ONE_TIME_EVENT);
			event.setDate(LocalDate.ofInstant(booking.getStartDate().toInstant(), ZoneId.systemDefault()));
			eventManager.addEvent(event);
		});
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;
	}

	private void initToolBar() {
		HBox toolPane = new HBox(15);
		toolPane.setAlignment(Pos.CENTER_LEFT);
		toolPane.setPadding(new Insets(10));

		String iconPaint = "#767676";

		// Initialize all the icons
		FontAwesomeIconView hideButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.NAVICON);
		FontAwesomeIconView prevWeekButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_LEFT);
		FontAwesomeIconView nextWeekButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_RIGHT);
		FontAwesomeIconView viewButtonIcon = new FontAwesomeIconView(
				FontAwesomeIcon.ANGLE_DOWN);

		FontAwesomeIconView searchButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
		FontAwesomeIconView csvButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.CLIPBOARD);

		hideButtonIcon.setFill(Paint.valueOf(iconPaint));
		prevWeekButtonIcon.setFill(Paint.valueOf(iconPaint));
		nextWeekButtonIcon.setFill(Paint.valueOf(iconPaint));
		viewButtonIcon.setFill(Paint.valueOf(iconPaint));

		searchButtonIcon.setFill(Paint.valueOf(iconPaint));

		hideButtonIcon.setSize("24");
		prevWeekButtonIcon.setSize("18");
		nextWeekButtonIcon.setSize("18");
		viewButtonIcon.setSize("18");

		searchButtonIcon.setSize("18");

		// The Navicon Button on the top left
		menuButton = new JFXButton();
		menuButton.getStyleClass().add("circle_buttom");
		menuButton.setGraphic(hideButtonIcon);

		menuButton.setOnAction((ActionEvent evt) -> {
			if (leftPane.getTranslateX() != 0 || mainPane.getLeft() == null) {
				mainPane.setLeft(leftPane);
			} else {
				mainPane.setLeft(null);
			}
		});


		csvButton = new JFXButton();
		csvButton.getStyleClass().add("circle_button");
		csvButton.setGraphic(csvButtonIcon);
		Tooltip csvTooltip = new Tooltip("");
		csvButton.setTooltip(csvTooltip);

		csvButton.setOnAction((ActionEvent evt) -> {

			CSVDialog csvDialog = new CSVDialog(this);
			csvDialog.clear();
			csvDialog.show();

		});

		searchButton = new JFXButton();
		searchButton.getStyleClass().add("circle_button");
		searchButton.setGraphic(searchButtonIcon);

		searchButton.setOnAction((ActionEvent evt) -> {

			SearchDialog eventDialog = new SearchDialog(this);
			eventDialog.clear();
			eventDialog.show();

		});

		// The Calendar Label
		Label calendarLabel = new Label("Calendar");
		calendarLabel.setId("calendar_label");

		// An Empty Space
		Pane spacerPane = new Pane();
		spacerPane.setMinWidth(15);
		spacerPane.setPrefWidth(30);

		// Today button with the date navigation controls
		todayButton = new JFXButton("Today");
		todayButton.getStyleClass().add("rectangle_button");

		JFXButton prevButton = new JFXButton();
		JFXButton nextButton = new JFXButton();

		prevButton.getStyleClass().add("circle_buttom");
		nextButton.getStyleClass().add("circle_buttom");

		prevButton.setGraphic(prevWeekButtonIcon);
		nextButton.setGraphic(nextWeekButtonIcon);

		prevButton.setOnAction(e -> {
			moveBackward();
		});

		nextButton.setOnAction(e -> {
			moveForward();
		});

		dateLabel = new Label();
		dateLabel.setId("dateLabel");

		// An Empty Space
		Pane grownedEmptyPane = new Pane();
		HBox.setHgrow(grownedEmptyPane, Priority.ALWAYS);

		// The control pane showing (day,week,month,year)
		HBox viewPane = createViewPane();

		// Add all components
		toolPane.getChildren().addAll(menuButton, calendarLabel, spacerPane, todayButton,
				prevButton, nextButton, dateLabel, grownedEmptyPane, viewPane, csvButton, searchButton);

		// Set toolPane on top of the BorderPane
		mainPane.setTop(toolPane);

		todayButton.setOnAction(e -> {
			// re-init the date
			selectedDate = LocalDate.now();

			if (navigationCalendar != null) {
				// Refresh navigation Calendar
				navigationCalendar.setSelectedDate(selectedDate);
				navigationCalendar.select(selectedDate.getDayOfMonth());
				navigationCalendar.refreshCalendar();
			}

			// Refresh main calendar
			displayCalendarBy(currentView);
		});

		todayButton.fire();
	}

	private void updateDate() {

		// Change the label

		dateLabel.setText(CalendarEvent.MONTHS[selectedDate.getMonthValue() - 1] + " "
				+ selectedDate.getYear());
	}

	private HBox createViewPane() {

		ToggleGroup group = new ToggleGroup();
		dayButton = new ToggleButton("Day");
		weekButton = new ToggleButton("Week");
		monthButton = new ToggleButton("Month");

		dayButton.setOnAction(e -> {
			if (!dayButton.isSelected()) {
				dayButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(DAY);
				currentView = DAY;
			}
		});

		weekButton.setOnAction(e -> {
			if (!weekButton.isSelected()) {
				weekButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(WEEK);
				currentView = WEEK;
			}
		});

		monthButton.setOnAction(e -> {
			if (!monthButton.isSelected()) {
				monthButton.setSelected(true);
				e.consume();
			} else {
				displayCalendarBy(MONTH);
				currentView = MONTH;
			}
		});

		dayButton.setToggleGroup(group);
		weekButton.setToggleGroup(group);
		monthButton.setToggleGroup(group);

		dayButton.setStyle("-fx-background-radius: 5 0 0 5");
		weekButton.setStyle("-fx-background-radius: 0 0 0 0");
		monthButton.setStyle("-fx-background-radius: 0 5 5 0");

		final int size = 27;

		dayButton.setPrefHeight(size);
		weekButton.setPrefHeight(size);
		monthButton.setPrefHeight(size);

		HBox viewPane = new HBox();
		viewPane.setAlignment(Pos.CENTER);
		viewPane.getChildren().addAll(dayButton, weekButton, monthButton);

		dayButton.setSelected(true);
		return viewPane;
	}

	private void displayCalendarBy(int displayMode) {
		this.currentView = displayMode;
		if (displayMode == DAY) {
			mainPane.setCenter(calendarDayView);
			calendarDayView.setAddButtonEnable(false);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (displayMode == WEEK) {
			mainPane.setCenter(calendarWeekView);
			calendarWeekView.setAddButtonEnable(true);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (displayMode == MONTH) {
			mainPane.setCenter(calendarMonthView);
			calendarMonthView.setAddButtonEnable(true);
			calendarMonthView.refreshCalendar(selectedDate);
		}
		updateDate();
	}

	private void initLeftPane() {

		leftPane = new VBox(5);
		leftPane.setPrefWidth(260);
		leftPane.setMinWidth(260);
		leftPane.setMaxWidth(260);

		navigationCalendar = new NavigationCalendar();

		navigationDateProperty = navigationCalendar.getSelectedDateProperty();

		navigationDateProperty.addListener(e -> {
			selectedDate = navigationCalendar.getLocalDate();
			displayCalendarBy(currentView);
		});

		mainPane.setLeft(leftPane);

		leftPane.getChildren().add(navigationCalendar);

		// CheckBox initiliazation
		VBox checkBoxPane = new VBox(9);
		checkBoxPane.setAlignment(Pos.CENTER_LEFT);
		VBox.setMargin(checkBoxPane, new Insets(0, 0, 0, 45));

		Label displayEventLabel = new Label("Events");
		displayEventLabel.setStyle("-fx-font-size : 16;");
		displayEventLabel.setPadding(new Insets(0, 0, 5, -20));

		JFXCheckBox pendingCheckBox = new JFXCheckBox("Pending");
		JFXCheckBox confirmedCheckBox = new JFXCheckBox("Confirmed");
		JFXCheckBox holidayCheckBox = new JFXCheckBox("Holiday");
		JFXCheckBox cancelledCheckBox = new JFXCheckBox("Expired / Cancelled");

		confirmedFilterProperty = confirmedCheckBox.selectedProperty();
		pendingFilterProperty = pendingCheckBox.selectedProperty();
		holidayFilterProperty = holidayCheckBox.selectedProperty();
		cancelledFilterProperty = cancelledCheckBox.selectedProperty();

		pendingCheckBox.setStyle(
				"-jfx-checked-color: #F7C531; -jfx-unchecked-color : #F7C531 ;");
		confirmedCheckBox.setStyle(
				"-jfx-checked-color : #81C457; -jfx-unchecked-color : #81C457 ;");
		cancelledCheckBox.setStyle(
				"-jfx-checked-color : #E85569; -jfx-unchecked-color : #E85569 ;");
		holidayCheckBox.setStyle(
				"-jfx-checked-color : #4C95CE; -jfx-unchecked-color : #4C95CE ;");

		pendingCheckBox.fire();
		confirmedCheckBox.fire();
		cancelledCheckBox.fire();
		holidayCheckBox.fire();

		FontAwesomeIconView starView = new FontAwesomeIconView(FontAwesomeIcon.STAR);
		FontAwesomeIconView flagView = new FontAwesomeIconView(FontAwesomeIcon.FLAG);
		FontAwesomeIconView tagView = new FontAwesomeIconView(FontAwesomeIcon.TAG);

		starView.setFill(Color.web("#FFD907"));
		flagView.setFill(Color.web("#2C3AB8"));
		tagView.setFill(Color.web("#B55231"));

		starView.setSize("18");
		flagView.setSize("18");
		tagView.setSize("18");

		checkBoxPane.getChildren().addAll(displayEventLabel, pendingCheckBox,
				confirmedCheckBox, cancelledCheckBox, holidayCheckBox);

		leftPane.getChildren().add(checkBoxPane);

	}

	private void initMainPane() {
		calendarDayView = new CalendarDayView(this, eventManager);
		calendarWeekView = new CalendarWeekView(this, eventManager);
		calendarMonthView = new CalendarMonthView(this, eventManager);

		mainPane.setCenter(calendarDayView);
	}

	private void moveBackward() {
		LocalDate previousDate = selectedDate;
		if (currentView == DAY) {
			selectedDate = selectedDate.minusDays(1);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (currentView == WEEK) {
			selectedDate = selectedDate.minusWeeks(1);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (currentView == MONTH) {
			selectedDate = selectedDate.minusMonths(1);
			calendarMonthView.refreshCalendar(selectedDate);
		}

		addPasquaAndPasquetta(previousDate);

		updateDate();
	}

	private void moveForward() {
		LocalDate previousDate = selectedDate;
		if (currentView == DAY) {
			selectedDate = selectedDate.plusDays(1);
			calendarDayView.refreshCalendar(selectedDate);
		} else if (currentView == WEEK) {
			selectedDate = selectedDate.plusWeeks(1);
			calendarWeekView.refreshCalendar(selectedDate);
		} else if (currentView == MONTH) {
			selectedDate = selectedDate.plusMonths(1);
			calendarMonthView.refreshCalendar(selectedDate);
		}

		addPasquaAndPasquetta(previousDate);

		updateDate();
	}

	private void addPasquaAndPasquetta(LocalDate previousDate) {
		// Add non-fixed festivities
		if(previousDate.getYear() != selectedDate.getYear()) {
			Calendar pasqua = ItalianHolidaysUtils.getInstance().getEasterForYear(selectedDate.getYear());
			Calendar pasquetta = ItalianHolidaysUtils.getInstance().getPasquettaForYear(selectedDate.getYear());

			int pasquaMonth = pasqua.get(Calendar.MONTH) + 1;
			if (pasquaMonth > 12) {
				pasquaMonth -= 12;
			}

			LocalDate pasquaDate = LocalDate.of(pasqua.get(Calendar.YEAR), pasquaMonth, pasqua.get(Calendar.DAY_OF_MONTH));
			eventManager.getEventsOn(pasquaDate).removeAll();

			CalendarEvent pasquaEvent = new CalendarEvent("Pasqua", CalendarEvent.HOLIDAY, "Chiuso per festività");
			pasquaEvent.setId(-1);
			pasquaEvent.setType(CalendarEvent.ONE_TIME_EVENT);
			pasquaEvent.setDate(pasquaDate);
			eventManager.addEvent(pasquaEvent);



			int pasquettaMonth = pasquetta.get(Calendar.MONTH) + 1;
			if (pasquettaMonth > 12) {
				pasquettaMonth -= 12;
			}

			LocalDate pasquettaDate = LocalDate.of(pasquetta.get(Calendar.YEAR), pasquettaMonth, pasquetta.get(Calendar.DAY_OF_MONTH));
			eventManager.getEventsOn(pasquaDate).removeAll();

			CalendarEvent pasquettaEvent = new CalendarEvent("Pasquetta", CalendarEvent.HOLIDAY, "Chiuso per festività");
			pasquettaEvent.setId(-1);
			pasquettaEvent.setType(CalendarEvent.ONE_TIME_EVENT);
			pasquettaEvent.setDate(pasquettaDate);
			eventManager.addEvent(pasquettaEvent);

		}
	}

	public void removeEvent(CalendarEvent event) {
		eventManager.removeEvent(event);
	}

	public ObservableList<CalendarEvent> getEvents(int dayOfMonth, int month, int year) {
		return eventManager.getEventsOn(LocalDate.of(year, month, dayOfMonth));
	}

	public BooleanProperty getConfirmedFilterProperty() {
		return confirmedFilterProperty;
	}

	public BooleanProperty getPendingFilterProperty() {
		return pendingFilterProperty;
	}

	public BooleanProperty getHolidayFilterProperty() {
		return holidayFilterProperty;
	}

	public BooleanProperty getCancelledFilterProperty() {
		return cancelledFilterProperty;
	}

	public BooleanProperty getCompletedFilterProperty() {
		return completedFilterProperty;
	}

	public void selectDate(LocalDate date) {
		selectedDate = date;
		dayButton.fire();
	}

	public void refreshCalendar() {
		if (currentView == 0) {
			calendarDayView.refreshCalendar(selectedDate);
		} else {
			calendarWeekView.refreshCalendar(selectedDate);
		}
	}
}