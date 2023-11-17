package it.voltats.gestionepista.ui.views;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CalendarWeekView extends CalendarView {

	private ScrollPane scrollPane;
	private HBox daysPane;

	private CalendarEventManager eventManager;

	public CalendarWeekView(JFXCalendar parentPane, CalendarEventManager eventManager) {
		super(parentPane);

		this.eventManager = eventManager;

		scrollPane = new ScrollPane();
		scrollPane.setPrefSize(650, 500);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setStyle("-fx-background-color: WHITE;");
		getChildren().add(0, scrollPane);

		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

		daysPane = new HBox();
		daysPane.setMaxWidth(Double.MAX_VALUE);
		scrollPane.setContent(daysPane);

	}

	public void refreshCalendar(LocalDate selectedDate) {

		clearEvents();

		// Go backward to get Sunday
		LocalDate monday = selectedDate;
		while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
			monday = monday.minusDays(1);
		}

		Queue<LocalDate> stack = new LinkedList<LocalDate>();
		String dayText[] = CalendarEvent.DAYS_FULL_NAMES;
		for (int i = 0; i < 7; i++) {
			VBox columnEntry = new VBox();
			VBox.setVgrow(columnEntry, Priority.ALWAYS);
			columnEntry.getStyleClass().add("calendar_cell");
			columnEntry.prefWidthProperty().bind(daysPane.widthProperty().divide(7.0));
			columnEntry.setPrefHeight(97);
			columnEntry.setPadding(new Insets(3, 0, 0, 5));

			Label dayName = new Label(dayText[i]);
			dayName.setPadding(new Insets(5, 0, 0, 5));

			Label dayCount = new Label(String.valueOf(monday.getDayOfMonth()));
			dayCount.getStyleClass().add("calendar_label_xl");

			VBox dayTitlePane = new VBox(dayName, dayCount);

			columnEntry.getChildren().addAll(dayTitlePane);
			columnEntry.setSpacing(25);

			daysPane.getChildren().add(columnEntry);
			stack.add(monday);
			monday = monday.plusDays(1);
		}

		addEvents(stack);
	}

	private void addEvents(Queue<LocalDate> stack) {
		int index = 0;

		while (stack.size() > 0) {
			LocalDate date = stack.remove();

			ObservableList<CalendarEvent> eventList = eventManager.getEventsOn(date);
			for (CalendarEvent event : eventList) {

				int eventPriority = event.getPriority();

				MiniEventPane eventBox = new MiniEventPane(event);
				eventBox.setOnMouseClicked(e -> {
					rootParentPane.selectDate(date);
				});

				if (eventPriority == CalendarEvent.CONFIRMED) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getConfirmedFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getConfirmedFilterProperty());
				} else if (eventPriority == CalendarEvent.PENDING) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getPendingFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getPendingFilterProperty());
				} else if (eventPriority == CalendarEvent.HOLIDAY) {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getHolidayFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getHolidayFilterProperty());
				} else {
					eventBox.visibleProperty()
							.bind(super.rootParentPane.getCancelledFilterProperty());
					eventBox.managedProperty()
							.bind(super.rootParentPane.getCancelledFilterProperty());
				}

				((VBox) daysPane.getChildren().get(index)).getChildren().add(eventBox);
				// eventPane.getChildren().add(eventBox);
			}

			index++;
		}

	}

	private void clearEvents() {
		daysPane.getChildren().clear();
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;

		refreshCalendar(LocalDate.now());
	}

	public void setAddButtonEnable(boolean b) {
		addButtonBooleanProperty.set(b);
	}

}