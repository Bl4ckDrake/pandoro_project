package it.voltats.gestionepista.db.impl;

import it.voltats.gestionepista.db.DbHelper;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.repo.BookingRepo;

import java.sql.Connection;

public class BookingRepoImpl implements BookingRepo {
    private final Connection connection = DbHelper.getConnection();

    @Override
    public void insert(Booking booking) {
        final String QUERY = "INSERT INTO booking(user_id, start_date, end_date, confirmed) VALUES(?,?,?,?)";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getUserId());
            statement.setString(2, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(booking.getStartDate()));
            statement.setString(3, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(booking.getEndDate()));
            statement.setBoolean(4, booking.isConfirmed());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Booking booking) {
        final String QUERY = "UPDATE booking SET (user_id, start_date, end_date, confirmed) VALUES(?,?,?,?) WHERE id=?";

        try {
            var statement = connection.prepareStatement(QUERY);
            statement.setInt(1, booking.getUserId());
            statement.setString(2, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(booking.getStartDate()));
            statement.setString(3, new SimpleDateFormat("dd-MM-yyyy HH:mm").format(booking.getEndDate()));
            statement.setBoolean(4, booking.isConfirmed());
            statement.setString(5, String.valueOf(booking.getId()));
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
            statement.setString(1, String.valueOf(booking.getId()));
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

            statement.setString(1, String.valueOf(userId));

            ResultSet resultSet = statement.executeQuery();

            List<Booking> result = new ArrayList<>();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getInt("user_id"),
                        new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(resultSet.getString("start_date")),
                        new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(resultSet.getString("end_date")),
                        BookingStatus.valueOf(resultSet.getString("status"))
                    );

                result.add(booking);
            }

            return result;

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
