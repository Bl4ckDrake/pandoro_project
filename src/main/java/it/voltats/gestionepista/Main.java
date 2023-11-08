package it.voltats.gestionepista;

import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import it.voltats.gestionepista.test.CustomCalendarView;
import it.voltats.gestionepista.util.ItalianHolidaysUtils;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.impl.BookingRepoImpl;
import it.voltats.gestionepista.db.impl.UserRepoImpl;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;

import it.voltats.gestionepista.util.DateConvertUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;



// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    BookingRepoImpl bookingRepo = new BookingRepoImpl();
    UserRepoImpl userRepo = new UserRepoImpl();



    public static void main(String[] args) {
        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CustomCalendarView calendarView = new CustomCalendarView(); // (1)

        //calendarView.setShowAddCalendarButton(false);

        ItalianHolidaysUtils italianHolidaysUtils = ItalianHolidaysUtils.getInstance();
        java.util.Calendar[] calendar = italianHolidaysUtils.fixedHolidays;

        Calendar bookingsCalendar = new Calendar("Bookings");
        Calendar holidaysCalendar = new Calendar("Holidays");


        bookingRepo.findAll().forEach(booking -> {
            User user = userRepo.findById(booking.getUserId());
            Interval interval = new Interval(DateConvertUtils.asLocalDateTime(booking.getStartDate()), DateConvertUtils.asLocalDateTime(booking.getEndDate()));
            Entry entry = new Entry<>(user.getName() + " " + user.getSurname());
            entry.setInterval(interval);
            entry.setId(String.valueOf(booking.getId()));
            bookingsCalendar.addEntry(entry);
        });

        for (int i = 0; i < calendar.length; i++) {
            java.util.Calendar cal = calendar[i];
            LocalDate date = LocalDate.of(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1, cal.get(java.util.Calendar.DAY_OF_MONTH));
            Entry entry = new Entry<>("Holiday");
            entry.setInterval(date);
            entry.setFullDay(true);
            entry.setRecurrenceRule("FREQ=YEARLY");
            holidaysCalendar.addEntry(entry);
        }

        bookingsCalendar.setStyle(Style.STYLE1);
        holidaysCalendar.setStyle(Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(bookingsCalendar, holidaysCalendar);

        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)

        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        Scene scene = new Scene(calendarView);
        primaryStage.setTitle("Gestione Pista");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1300);
        primaryStage.setHeight(1000);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}