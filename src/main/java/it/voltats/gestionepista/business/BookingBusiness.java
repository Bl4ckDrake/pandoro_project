package it.voltats.gestionepista.business;

import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.model.Promotions;
import it.voltats.gestionepista.db.impl.BookingRepoImpl;
import it.voltats.gestionepista.util.ItalianHolidaysUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class BookingBusiness {
    private static final BookingRepoImpl bookings = new BookingRepoImpl();
    private static final ItalianHolidaysUtils italianHolidaysUtil = ItalianHolidaysUtils.getInstance();

    public boolean insert(Booking booking){
        if(!italianHolidaysUtil.isWeekendOrHoliday(italianHolidaysUtil.fromDate(booking.getStartDate()))
            && italianHolidaysUtil.getEasterForYear(Integer.parseInt(new SimpleDateFormat("yyyy").format(booking.getStartDate()))).compareTo(italianHolidaysUtil.fromDate(booking.getStartDate())) != 0
                && bookings.isAvaiable(booking.getStartDate(), booking.getEndDate())) {
            booking.setPrice(totalCost(booking));
            bookings.insert(booking);
            return true;
        }
        return false;
    }

    public boolean delete(Booking booking){
        LocalDate bookingDate = booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(bookingDate.isAfter(LocalDate.now())) {
            bookings.delete(booking);
            return true;
        }
        return false;
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
            result -= result * Promotions.NOTTURNA.getValue();

        switch ((int)diffHours){
            case 8:
                result -= result * Promotions.OTTO_ORE.getValue();
                break;

            case 7:
            case 6:
                result -= result * Promotions.SEI_ORE.getValue();
                break;

            case 5:
            case 4:
                result -= result * Promotions.QUATTRO_ORE.getValue();
                break;
        }

        if (ItalianHolidaysUtils.getInstance().isWeekendOrHoliday(ItalianHolidaysUtils.getInstance().fromDate(booking.getStartDate())))
            result -= result * Promotions.FESTIVO.getValue();

        return result;
    }

}
