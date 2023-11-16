package it.voltats.gestionepista.db.repo;

import it.voltats.gestionepista.db.entity.Booking;

import java.util.Date;
import java.util.List;

public interface BookingRepo {
    public void insert(Booking booking);
    public void update(Booking booking);
    public void delete(Booking booking);
    public void storeFinishedBooking(Booking booking);
    public Booking findById(int id);
    public Booking findByStartDateAndEndDate(Date startDate, Date endDate);
    public List<Booking> findAllByUserId(int userId);
    public List<Booking> findAllByDate(Date date);
    public List<Booking> findAll();
    public boolean isAvaiable(Date requestedStartDate, Date requestedEndDate);
}
