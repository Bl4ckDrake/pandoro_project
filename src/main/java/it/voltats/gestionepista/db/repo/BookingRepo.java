package it.voltats.gestionepista.db.repo;

import it.voltats.gestionepista.db.entity.Booking;

public interface BookingRepo {
    public void insert(Booking booking);
    public void update(Booking booking);
    public void delete(Booking booking);
    public List<Booking> findAllByUserId(int userId);
    public List<Booking> findAll();
}
