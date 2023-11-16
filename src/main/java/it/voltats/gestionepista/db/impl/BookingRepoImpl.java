package it.voltats.gestionepista.db.impl;

import it.voltats.gestionepista.db.DbHelper;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.repo.BookingRepo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingRepoImpl implements BookingRepo {
    private final Connection connection = DbHelper.getConnection();

    @Override
    public void insert(Booking booking) {
        final String QUERY = "INSERT INTO booking(user_id, start_date, end_date, status, price) VALUES(?,?,?,?,?)";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getUserId());
            statement.setString(2, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getStartDate()));
            statement.setString(3, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getEndDate()));
            statement.setString(4, booking.getStatus().name());
            statement.setDouble(5, booking.getPrice());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Booking booking) {
        final String QUERY = "UPDATE booking SET (user_id, start_date, end_date, status, price) = (?,?,?,?,?) WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getUserId());
            statement.setString(2, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getStartDate()));
            statement.setString(3, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getEndDate()));
            statement.setString(4, booking.getStatus().name());
            statement.setDouble(5, booking.getPrice());
            statement.setInt(6, booking.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void storeFinishedBooking(Booking booking) {

        final String QUERY = "UPDATE booking SET (user_id, start_date, end_date, status, price) = (?,?,?,?,?) WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getUserId());
            statement.setString(2, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getStartDate()));
            statement.setString(3, new SimpleDateFormat("dd/MM/yyyy HH:mm").format(booking.getEndDate()));
            statement.setString(4, BookingStatus.STORED.name());
            statement.setDouble(5, booking.getPrice());
            statement.setInt(6, booking.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Booking booking) {
        final String QUERY = "DELETE FROM booking WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Booking> findAllByUserId(int userId) {
        final String QUERY = "SELECT * FROM booking WHERE user_id=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            List<Booking> result = new ArrayList<>();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("user_id"),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("start_date")),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("end_date")),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getDouble("price")
                    );

                booking.setId(resultSet.getInt("id"));

                result.add(booking);
            }

            return result;

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Booking> findAll() {
        final String QUERY = "SELECT * FROM booking";

        try {
            var statement = connection.prepareStatement(QUERY);

            ResultSet resultSet = statement.executeQuery();

            List<Booking> result = new ArrayList<>();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("user_id"),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("start_date")),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("end_date")),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getDouble("price")
                );
                booking.setId(resultSet.getInt("id"));

                result.add(booking);
            }

            return result;
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean isAvaiable(Date requestedStartDate, Date requestedEndDate) {

        if((requestedEndDate.getTime() - requestedStartDate.getTime())/(60*60*1000) > 8)
            return false;

        final String QUERY = "SELECT * FROM booking";
        List<Booking> bookings = new ArrayList<>();

        try {
            var statement = connection.prepareStatement(QUERY);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("user_id"),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("start_date")),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("end_date")),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getDouble("price")
                );

                bookings.add(booking);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        for(Booking b: bookings){
            if(b.getStartDate().getYear() == requestedStartDate.getYear() &&
                    b.getStartDate().getMonth() == requestedStartDate.getMonth() &&
                    b.getStartDate().getDate() == requestedStartDate.getDate()){
                for(int h = b.getStartDate().getHours(); h<b.getEndDate().getHours(); h++){
                    if(h == requestedStartDate.getHours())
                        return false;   //CONTROLLO ORE
                } if(b.getEndDate().getMinutes() > requestedEndDate.getMinutes())
                    return false;   //CONTROLLO MINUTI
            }

        }   return true;

        /*for (Booking booking: bookings) {
            if ((booking.getStartDate().compareTo(requestedStartDate) >= 0 && booking.getEndDate().compareTo(requestedStartDate) <= 0)
                            || (booking.getStartDate().compareTo(requestedEndDate) >= 0 && booking.getEndDate().compareTo(requestedEndDate) <= 0))
                return false;
        }

        return true;*/
    }

    @Override
    public Booking findById(int id) {
        final String QUERY = "SELECT * FROM booking WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);

            Booking result;

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.getString("start_date") == null ||
                    resultSet.getString("end_date") == null ||
                    resultSet.getString("status") == null)
                return null;

            result = new Booking(
                    resultSet.getInt("user_id"),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("start_date")),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("end_date")),
                    BookingStatus.valueOf(resultSet.getString("status")),
                    resultSet.getDouble("price")
            );
            result.setId(resultSet.getInt("id"));

            return result;

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Booking> findAllByDate(Date date){
        final String QUERY = "SELECT * FROM booking";
        List<Booking> bookings = new ArrayList<>();

        try {
            var statement = connection.prepareStatement(QUERY);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("user_id"),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("start_date")),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(resultSet.getString("end_date")),
                        BookingStatus.valueOf(resultSet.getString("status")),
                        resultSet.getDouble("price")
                );
                booking.setId(resultSet.getInt("id"));

                bookings.add(booking);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        List<Booking> result = new ArrayList<>();
        for(Booking booking: bookings){
            if(new SimpleDateFormat("dd/MM/yyyy").format(booking.getStartDate()).equals(new SimpleDateFormat("dd/MM/yyyy").format(date)))
                result.add(booking);
        }

        return result;
    }
}
