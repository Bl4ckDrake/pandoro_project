package it.voltats.gestionepista.ui.controllers;

import com.jfoenix.controls.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.impl.BookingRepoImpl;
import it.voltats.gestionepista.db.impl.UserRepoImpl;
import it.voltats.gestionepista.ui.dialog.DialogHandler;
import it.voltats.gestionepista.util.CSVUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CSVDialogController {

    @FXML
    private ComboBox selectExportType;

    // Import inputs
    @FXML
    private JFXButton chooseFileButton;
    @FXML
    private JFXButton importConfirmButton;

    // Export inputs
    @FXML
    private JFXButton choosePathButton;
    @FXML
    private JFXTextField exportTextField;
    @FXML
    private JFXButton exportConfirmButton;


    // Table selection
    private int selectedTable;

    private final int TABLE_USER = 0;
    private final int TABLE_BOOKING = 1;

    // File / Path choice
    private File fileChoice;
    private File pathChoice;

    // Repos
    private UserRepoImpl userRepo = new UserRepoImpl();
    private BookingRepoImpl bookingRepo = new BookingRepoImpl();
    @FXML
    public void initialize() {

        ObservableList<String> options =
                FXCollections.observableArrayList();

        options.add("User Table");
        options.add("Booking Table");

        selectExportType.setItems(options);

        selectExportType.valueProperty().addListener((observableValue, o, t1) -> {

            if(t1 == null) {
                selectedTable = -1;
                return;
            }

            if(t1.equals("User Table")) {
                selectedTable = TABLE_USER;
                return;
            }

            if(t1.equals("Booking Table")) {
                selectedTable = TABLE_BOOKING;
            }

        });

        // Choose File
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Import File");
            fileChoice = fileChooser.showOpenDialog(new Stage());
        });

        // Choose Path
        choosePathButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose Export File Path");
            pathChoice = directoryChooser.showDialog(new Stage());
        });


        // Confirm button icons
        FontAwesomeIconView importConfirmIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
        importConfirmIcon.setSize("18");
        importConfirmIcon.setFill(Paint.valueOf("#767676"));

        FontAwesomeIconView exportConfirmIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK);
        importConfirmIcon.setSize("18");
        importConfirmIcon.setFill(Paint.valueOf("#767676"));


        // Set icons to buttons
        importConfirmButton.setGraphic(importConfirmIcon);
        exportConfirmButton.setGraphic(exportConfirmIcon);


        // Set event listener on buttons
        importConfirmButton.setOnAction(e -> {
            if(selectedTable == -1 || fileChoice == null) {
                return;
            }

            CSVUtil csvUtil = new CSVUtil();

            if(selectedTable == TABLE_BOOKING) {
                System.out.println(fileChoice.getAbsolutePath());
                List<Booking> importedBookings = csvUtil.importBookingsFromCSV(fileChoice.getAbsolutePath());
                for (Booking booking: importedBookings) {
                    bookingRepo.insert(booking);
                }
                DialogHandler.showConfirmationDialog("Imported " + importedBookings.size() + " bookings");
                return;
            }

            if(selectedTable == TABLE_USER) {
                List<User> importedUsers = csvUtil.importUserListFromCSV(fileChoice.getAbsolutePath());
                for(User user: importedUsers) {
                    userRepo.insert(user);
                }
                DialogHandler.showConfirmationDialog("Imported " + importedUsers.size() + " users");
            }
        });

        exportConfirmButton.setOnAction(e -> {
            if(exportTextField.getText().isBlank() || pathChoice == null) {
                return;
            }

            CSVUtil csvUtil = new CSVUtil();

            if(selectedTable == TABLE_BOOKING) {
                csvUtil.exportBookingsToCSV(pathChoice.getAbsolutePath() + "/" + exportTextField.getText(), bookingRepo.findAll());
                DialogHandler.showConfirmationDialog("Exported " + bookingRepo.findAll().size() + " bookings to " + pathChoice.getAbsolutePath() + "/" + exportTextField.getText());
                return;
            }

            if(selectedTable == TABLE_USER) {
                csvUtil.exportUserListToCSV(pathChoice.getAbsolutePath() + "/" + exportTextField.getText(), userRepo.findAll());
                DialogHandler.showConfirmationDialog("Exported " + userRepo.findAll().size() + " users to " + pathChoice.getAbsolutePath() + "/" + exportTextField.getText());
            }
        });
    }




    public void clear() {
        selectExportType.valueProperty().setValue(null);
    }
}