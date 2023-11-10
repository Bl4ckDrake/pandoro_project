package it.voltats.gestionepista.business;

import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.Promozioni;
import it.voltats.gestionepista.db.impl.BookingRepoImpl;
import it.voltats.gestionepista.util.ItalianHolidaysUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class BookingBusiness {
    private static final BookingRepoImpl bookings = new BookingRepoImpl();

    public void add(Booking booking){
        if(isAvailable(booking))
            bookings.insert(booking);
    }

    private boolean isAvailable(Booking booking){
        if((booking.getEndDate().getTime() - booking.getStartDate().getTime())/(60*60*1000) > 8)
            return false;   //PRENOTAZIONE MAX 8 ORE

        for(Booking b: bookings.findAll()){
            if(b.getStartDate().getYear() == booking.getStartDate().getYear() &&
                    b.getStartDate().getMonth() == booking.getStartDate().getMonth() &&
                    b.getStartDate().getDate() == booking.getStartDate().getDate()){
                for(int h = b.getStartDate().getHours(); h<b.getEndDate().getHours(); h++)
                    if(h == booking.getStartDate().getHours())
                        return false;   //CONTROLLO ORE
                if(b.getEndDate().getMinutes() > booking.getEndDate().getMinutes())
                    return false;   //CONTROLLO MINUTI
            }

        }

        return true;
    }

    public void delete(Booking booking){
        LocalDate bookingDate = booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(bookingDate.isAfter(LocalDate.now()))
            bookings.delete(booking);
    }

    public Booking findById(int id){
        return bookings.findById(id);
    }

    public void update(Booking booking){
        bookings.update(booking);
    }

    public List<Booking> findAll(){
        return bookings.findAll();
    }

    public List<Booking> findAllByUserId(int userId){
        return bookings.findAllByUserId(userId);
    }

    public List<Booking> findAllByDate(Date date) {
        return bookings.findAllByDate(date);
    }

    public double totalCost(Booking booking) {
        long diff = booking.getEndDate().getTime() - booking.getStartDate().getTime();
        long diffHours = diff / (60 * 60 * 1000);
        double result = diffHours * 1200;

        if (booking.getEndDate().getHours() >= 20 || booking.getEndDate().getHours() == 0)
            result -= result * Promozioni.NOTTURNA.getValue();

        switch ((int)diffHours){
            case 8:
                result -= result * Promozioni.OTTO_ORE.getValue();
                break;

            case 7:
            case 6:
                result -= result * Promozioni.SEI_ORE.getValue();
                break;

            case 5:
            case 4:
                result -= result * Promozioni.QUATTRO_ORE.getValue();
                break;
        }

        if (HolidayCalendar.isHoliday(booking.getStartDate()))
            result -= result * Promozioni.FESTIVO.getValue();

        return result;
    }

}
