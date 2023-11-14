package it.voltats.gestionepista.util;

import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.entity.model.Gender;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class CSVUtil {

    public List<Booking> importBookingsFromCSV(String filename) {

        List<Booking> bookings = new ArrayList<>();
        Path pathToFile = Paths.get(filename + ".csv");

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {

            String line = br.readLine();

            while (line != null) {

                String[] params = line.split(",");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                try {
                    Booking booking = new Booking(Integer.parseInt(params[0]), dateFormat.parse(params[1]), dateFormat.parse(params[2]), BookingStatus.valueOf(params[3]), Double.parseDouble(params[4]));
                    bookings.add(booking);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                br.readLine();
            }
        } catch (IOException e) {

        }

        return bookings;
    }

    public boolean exportBookingsToCSV(String filename, List<Booking> bookings) {
        try (FileWriter write = new FileWriter(filename + "_bookings.csv")) {

            for (Booking booking : bookings) {
                String csvLine = String.format("%s,%s,%s,%s,%s,%s", booking.getId(), booking.getStartDate(), booking.getEndDate(), booking.getPrice(), booking.getStatus());
                write.write(csvLine);
            }

        }catch (IOException e) {
            return false;
        }
        return true;
    }


    public List<User> importUserListFromCSV(String filename) {
        List<User> userList = new ArrayList<>();
        Path pathToFile = Paths.get(filename + ".csv");

        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {

            String line = br.readLine();

            while (line != null) {
                String[] params = line.split(",");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    User user = new User(params[0], params[1], Gender.valueOf(params[2]), dateFormat.parse(params[3]), params[4], params[5], params[6]);
                    userList.add(user);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                line = br.readLine();
            }
        } catch (IOException e) {

        }

        return userList;
    }

    public boolean exportUserListToCSV(String filename, List<User> userList) {
        try (FileWriter writer = new FileWriter(filename + "_users.csv")) {

            for (User user : userList) {
                String csvLine = String.format("%s,%s,%s,%s,%s,%s,%s", user.getName(), user.getSurname(), user.getGender(), user.getBirthdate(), user.getCf(), user.getEmail(), user.getPhoneNumber());
                writer.write(csvLine + "\n");
            }

        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
