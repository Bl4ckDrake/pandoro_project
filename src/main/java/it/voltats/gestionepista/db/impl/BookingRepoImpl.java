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
            statement.setString(2, booking.getStartDate().toString());
            statement.setString(3, booking.getEndDate().toString());
            statement.setBoolean(4, booking.isConfirmed());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Booking booking) {

    }

    @Override
    public void delete(Booking booking) {

    }
}
