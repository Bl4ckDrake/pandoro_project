package it.voltats.gestionepista.ui.views;

import java.time.LocalDate;

import it.voltats.gestionepista.ui.model.CalendarEvent;
import it.voltats.gestionepista.ui.model.CalendarEventManager;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.VBox;

public class CalendarDayView extends CalendarView {

	private ScrollPane scrollPane;
	private VBox eventPane;

	private CalendarEventManager eventManager;

	public LocalDate currectDate;

	public CalendarDayView(JFXCalendar parentPane, CalendarEventManager eventManager) {
		super(parentPane);

		this.eventManager = eventManager;
		addEventDialog.setCalendarEventManager(eventManager);

		scrollPane = new ScrollPane();
		eventPane = new VBox();
		eventPane.setSpacing(25);
		eventPane.setMaxHeight(Double.MAX_VALUE);
		eventPane.setPadding(new Insets(0, 0, 10, 0));
		scrollPane.setContent(eventPane);
		scrollPane.setFitToHeight(true);

		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);

		eventPane.setStyle("-fx-background-color: WHITE");
		scrollPane.setStyle("-fx-background-color: WHITE");

		getChildren().add(0, scrollPane);

	}

	public void refreshCalendar(LocalDate selectedDate) {
		this.currectDate = selectedDate;
		clearEvents();

		String dayText[] = CalendarEvent.DAYS_FULL_NAMES;
		VBox columnEntry = new VBox();
		columnEntry.getStyleClass().add("calendar_cell");
		columnEntry.prefWidthProperty().bind(scrollPane.widthProperty());
		columnEntry.setPrefHeight(97);
		columnEntry.setPadding(new Insets(3, 0, 0, 10));

		Label dayName = new Label(dayText[selectedDate.getDayOfWeek().ordinal()]);
		dayName.setPadding(new Insets(5, 0, 0, 5));

		Label dayCount = new Label(String.valueOf(selectedDate.getDayOfMonth()));
		dayCount.getStyleClass().add("calendar_label_xl");

		columnEntry.getChildren().addAll(dayName, dayCount);
		eventPane.getChildren().add(columnEntry);

		addEvents(selectedDate);
	}

	private void addEvents(LocalDate selectedDate) {
		if (eventManager == null)
			return;

		ObservableList<CalendarEvent> eventList = eventManager.getEventsOn(selectedDate);

		for (CalendarEvent event : eventList) {
			int eventPriority = event.getPriority();


			EventPane eventBox = new EventPane(event);
			eventBox.setCalendarView(this);
			eventBox.setEventManager(eventManager);
			eventBox.setEventParentPane(eventPane);
			eventBox.setStackPaneRoot(rootParentPane);

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

			eventPane.getChildren().add(eventBox);
		}

	}

	private void clearEvents() {
		eventPane.getChildren().clear();
	}

	public void setEventManager(CalendarEventManager eventManager) {
		this.eventManager = eventManager;
		refreshCalendar(LocalDate.now());
	}

	public void setAddButtonEnable(boolean b) {
		addButtonBooleanProperty.set(b);
	}
}