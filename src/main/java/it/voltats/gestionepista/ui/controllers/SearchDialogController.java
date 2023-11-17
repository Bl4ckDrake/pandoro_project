package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class SearchDialogController {

    @FXML
    private ComboBox selectUserBox;
    @FXML
    private JFXButton resetUserButton;

    @FXML
    private DatePicker searchDatePicker;
    @FXML
    private JFXButton resetDateButton;

    @FXML
    private JFXTextField idTextField;


    @FXML
    private ListView resultListView;


    private int selectedUserId;

    private UserBusiness userBusiness = new UserBusiness();
    private BookingBusiness bookingBusiness = new BookingBusiness();

    @FXML
    public void initialize() {


        /* User filter */
        List<User> userList = userBusiness.findAll();

        ObservableList<String> options =
                FXCollections.observableArrayList();

        for (User user: userList) {
            options.add("ID: " + user.getId() + " " + user.getName() + " " + user.getSurname() + " " + user.getCf());
        }

        selectUserBox.setItems(options);

        selectUserBox.valueProperty().addListener((observableValue, o, t1) -> {

            if(t1 == null) {
                selectedUserId = -1;
                return;
            }

            selectedUserId = userList.get(options.indexOf(t1)).getId();
            idTextField.setText("");
            update();
        });

        /* Date filter */
        searchDatePicker.valueProperty().addListener(e -> {
            idTextField.setText("");
            update();
        });

        // Id text field
        idTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            searchDatePicker.setValue(null);
            selectUserBox.valueProperty().setValue(null);
            update();
        });

        // Button icons
        FontAwesomeIconView resetUserButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        resetUserButtonIcon.setFill(Paint.valueOf("#767676"));
        resetUserButtonIcon.setSize("24");

        FontAwesomeIconView resetDateButtonIcon = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        resetDateButtonIcon.setFill(Paint.valueOf("#767676"));
        resetDateButtonIcon.setSize("24");


        // Reset user selection button
        resetUserButton.setGraphic(resetUserButtonIcon);
        resetUserButton.setOnAction(e -> {
            selectUserBox.valueProperty().setValue(null);
            update();
        });

        // Reset date selection button
        resetDateButton.setGraphic(resetDateButtonIcon);
        resetDateButton.setOnAction(e -> {
            searchDatePicker.setValue(null);
            update();
        });
    }


    public void update() {
        ObservableList<String> bookings = FXCollections.observableArrayList();

        if(!idTextField.getText().isEmpty()) {
            Booking result = bookingBusiness.findById(Integer.parseInt(idTextField.getText()));

            if(result != null) {
                bookings.add(result.toString());
            }

            resultListView.setItems(bookings);
            return;
        }

        if(selectedUserId != -1 && searchDatePicker.getValue() != null) {
            for(Booking booking: bookingBusiness.findAllByUserIdAndDate(selectedUserId, Date.from(searchDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                bookings.add(booking.toString());
            }
            resultListView.setItems(bookings);
            return;
        }

        if(selectedUserId != -1) {
            for (Booking booking: bookingBusiness.findAllByUserId(selectedUserId)) {
                bookings.add(booking.toString());
            }
            resultListView.setItems(bookings);
            return;
        }

        if(searchDatePicker.getValue() != null) {
            for (Booking booking: bookingBusiness.findAllByDate(Date.from(searchDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                bookings.add(booking.toString());
            }
            resultListView.setItems(bookings);
            return;
        }

        bookings.removeAll();
        resultListView.setItems(bookings);
    }


    public void clear() {
        selectUserBox.valueProperty().setValue(null);
        searchDatePicker.setValue(null);
    }
}