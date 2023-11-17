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


    /**
     * Metodo per inserire una prenotazione nel database
     * Fa un controllo per vedere se e' possibile inserire la prenotazione
     * In modo che non rientri in altre prenotazioni o non capitino nelle festivita'
     * Viene anche richiamato il metodo per calcolare il costo in caso di esito positivo
     * @param booking prenotazione da inserire
     * @return true --> Inserito correttamente | false --> prenotazione non valida
     */
    public int insert(Booking booking){
        if(bookings.isAvaiable(booking.getStartDate(), booking.getEndDate())) {
            booking.setPrice(totalCost(booking));
            bookings.insert(booking);
            return bookings.findByStartDateAndEndDate(booking.getStartDate(), booking.getEndDate()).getId();
        }
        return -1;
    }

    /**
     * Metodo per eliminare una prenotazione
     * Viene controllato che la prentazione sia successivo al giorno di oggi
     * In caso non lo sia sara' impossibile cancellare
     * @param booking Prenotazione da cancellare
     * @return true --> prenotazione cancellata | false --> prenotazione non cancellata
     */
    public boolean delete(Booking booking){
        LocalDate bookingDate = booking.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if(bookingDate.isAfter(LocalDate.now())) {
            bookings.delete(booking);
            return true;
        }
        return false;
    }

    /**
     * Ritorna prenotazione con uno specifico ID
     * @param id ID da cercare
     * @return prenotazione trovata
     */
    public Booking findById(int id){
        return bookings.findById(id);
    }

    /**
     * Aggiornamento pagamento da Pending a Confirmed
     * @param booking prenotazione da aggiornare
     */
    public void update(Booking booking){
        bookings.update(booking);
    }

    /**
     * @return Ritorna lista di prenotazioni
     */
    public List<Booking> findAll(){
        return bookings.findAll();
    }

    /**
     * Ritorna lista di prenotazioni di un User
     * @param userId ID dell'user da cercare
     * @return lista prenotazioni collegato a un UserID specifico
     */
    public List<Booking> findAllByUserId(int userId){
        return bookings.findAllByUserId(userId);
    }

    /**
     * Ritorna lista di prenotazioni di un giorno specifico
     * @param date Giorno in cui cercare prenotazioni
     * @return lista prenotazioni di un giornno
     */
    public List<Booking> findAllByDate(Date date) {
        return bookings.findAllByDate(date);
    }

    /**
     * Ritorna il costo totale di una prenotazione
     * @param booking prenotazione da cui calcolare il suo prezzo
     * @return costo prenotazione
     */
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

    public List<Booking> findAllByUserIdAndDate(int userId, Date date) {
        return bookings.findAllByUserIdAnddate(userId, date);
    }
}
