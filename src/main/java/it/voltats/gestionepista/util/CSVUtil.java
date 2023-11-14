package it.voltats.gestionepista.util;

import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.BookingStatus;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class CSVUtil {

    public List<Booking> importCSVFromFile(String filename) {

        List<Booking> bookings = new ArrayList<>();
        Path pathToFile = Paths.get(filename);

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
        try (FileWriter write = new FileWriter(filename)) {

            for (Booking booking : bookings) {
                String csvLine = String.format("%d,%s,%s,%s,%s,%s", booking.getId(), booking.getStartDate(), booking.getEndDate(), booking.getPrice(), booking.getStatus());
                write.write(csvLine);
            }

        }catch (IOException e) {
            return false;
        }
        return true;
    }
}
