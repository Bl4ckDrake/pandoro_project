package it.voltats.gestionepista.presentation;

import it.voltats.gestionepista.business.BookingBusiness;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.entity.model.Gender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MenuManagement {
    BookingBusiness bookingBusiness;
    Scanner scanner;
    UserBusiness userBusiness;
    SimpleDateFormat dateFormat;
    SimpleDateFormat dateFormatBooking;
    IOManagement IOManager;

    public MenuManagement() {
        this.bookingBusiness = new BookingBusiness();
        this.userBusiness = new UserBusiness();
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.dateFormatBooking = new SimpleDateFormat("dd/MM/yyyy HH");
        this.IOManager = new IOManagement();
    }

    public void execute(){

        System.out.println("\n\n\n" +
                "\n" +
                "  ________                 __  .__                       __________.__          __          \n" +
                " /  _____/  ____   _______/  |_|__| ____   ____   ____   \\______   \\__| _______/  |______   \n" +
                "/   \\  ____/ __ \\ /  ___/\\   __\\  |/  _ \\ /    \\_/ __ \\   |     ___/  |/  ___/\\   __\\__  \\  \n" +
                "\\    \\_\\  \\  ___/ \\___ \\  |  | |  (  <_> )   |  \\  ___/   |    |   |  |\\___ \\  |  |  / __ \\_\n" +
                " \\______  /\\___  >____  > |__| |__|\\____/|___|  /\\___  >  |____|   |__/____  > |__| (____  /\n" +
                "        \\/     \\/     \\/                      \\/     \\/                    \\/            \\/ \n" +
                "\n\n\n");

        int choice;
        do {
            System.out.println("[1] --> Inserisci una nuova prenotazione ad un nuovo utente");
            System.out.println("[2] --> Inserisci una nuova prenotazione ad un utente esistente");
            System.out.println("[3] --> Modifica lo stato di pagamento di una prenotazione");
            System.out.println("[4] --> Rimuovi una prenotazione");
            System.out.println("[5] --> Cerca una prenotazione per ID");
            System.out.println("[6] --> Cerca le prenotazioni di una giornata");
            System.out.println("[7] --> Cerca le prenotazioni di un utente");
            System.out.println("[8] --> Lista utenti");
            System.out.println("[9] --> Lista prenotazioni");
            System.out.println("[0] --> Esci");
            System.out.print("Inserire un numero: ");
            String sChoice = scanner.nextLine();

            try{
                choice = Integer.parseInt(sChoice);
            }catch (NumberFormatException e) {
                choice = -1;
                System.err.println("\n\n" +
                        "ERRORE INPUT" +
                        "\n\n");
            }

            User user;
            Booking booking = null;
            String bookingIDString;
            int bookingID;

            switch (choice) {
                default:
                    System.err.println("Scelta errata");
                    break;

                case 1:
                    user = inputUser();

                    if (userBusiness.insert(user)) {
                        System.out.println("Cliente inserito nel database!");

                        booking = inputBooking(user);

                        if (bookingBusiness.insert(booking))
                            System.out.println("Prenotazione inserita nel database!");
                        else
                            System.err.println("\n\n" + "Orario non disponibile!" + "\n\n");

                    } else
                        System.err.println("\n\n" + "Cliente gia' presente o " +
                                "codice fiscale falso/errato o " + "età minima non soddisfatta" + "\n\n");


                    break;
                case 2:
                    user = userBusiness.findById(IOManager.getIntInput("Inserisci user id: "));

                    booking = inputBooking(user);

                    if (bookingBusiness.insert(booking))
                        System.out.println("Prenotazione inserita nel database!");
                    else
                        System.err.println("\n\n" + "Orario non disponibile!" + "\n\n");

                    break;
                case 3:
                        bookingID = IOManager.getIntInput("Inserisci l'ID della prenotazione da modificare");
                        booking = bookingBusiness.findById(bookingID);
                        if (booking != null) {
                            booking.setStatus(BookingStatus.CONFIRMED);
                            bookingBusiness.update(booking);
                            System.out.println("Prenotazione modificata!");
                        } else
                            System.err.println("Prenotazione non presente");

                    break;

                case 4:
                        bookingID = IOManager.getIntInput("Inserisci l'ID della prenotazione da rimuovere");
                        booking = bookingBusiness.findById(bookingID);
                        if (booking != null) {
                            bookingBusiness.delete(booking);
                            System.out.println("Prenotazione rimossa!");
                        } else
                            System.err.println("Prenotazione non presente");

                    break;

                case 5:
                    bookingIDString = IOManager.getStringInput("Inserisci l'ID della prenotazione che vuoi cercare");
                    try{
                        bookingID = Integer.parseInt(bookingIDString);
                        booking = bookingBusiness.findById(bookingID);
                        if (booking != null) {
                            System.out.println("Prenotazione trovata!" +
                                    "\n" + booking);
                        } else
                            System.err.println("Prenotazione non presente");
                    }catch (NumberFormatException e) {
                        System.err.println("\n\n" +
                                "ID non valido INPUT" +
                                "\n\n");
                    }
                    break;

                case 6:
                    String dateString;

                    do {
                        dateString = IOManager.getStringInput("Data da ricercare");
                    } while(!checkInput(dateString));

                    Date date = null;

                    try {
                        date = dateFormat.parse(dateString);

                        System.out.println("\n\nBOOKINGS:");

                        for(Booking b : bookingBusiness.findAllByDate(date)){
                            System.out.println(b);
                        }

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    break;
                case 7:
                    List<Booking> bookings = bookingBusiness.findAllByUserId(IOManager.getIntInput("Inserisci id utente"));

                    for(Booking b: bookings) {
                        System.out.println(b);
                    }
                    break;
                case 8:
                    System.out.println("\n\nUSERS: ");

                    for(User u : userBusiness.findAll()){
                        System.out.println(u);
                    }

                    break;
                case 9:
                    System.out.println("\n\nBOOKINGS: ");

                    for(Booking b: bookingBusiness.findAll()){
                        System.out.println(b);
                    }

                    break;
                case 0:
                    System.out.println("Bye bye.");
                    break;
            }


            System.out.println("\n\n");

        }while(choice != 0);
        scanner.close();

    }

    /**
     * Ritorna un oggetto User creato attraverso gli input datosi dall'utente
     * Non viene passato nessun argomento nel metodo
     * L'utente va ad inserire uno a uno gli attributi dell'oggetto User
     * @return  Object User
     */
    private User inputUser(){
        System.out.println("Inserimento dati cliente:");
        String name, surname, birthDateString, gender, cf, email, telephoneNumber;

        do {
            name = IOManager.getStringInput("Nome");
        }while(!checkInput(name));

        do{
        surname = IOManager.getStringInput("Cognome");
        }while(!checkInput(surname));

        do{
            birthDateString = IOManager.getStringInput("Data di nascita");
        }while(!checkInput(birthDateString));

        Date birthDate = null;

        try {
           birthDate = dateFormat.parse(birthDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        do{
        gender = IOManager.getStringInput("Genere (M o F)").toUpperCase();
        }while(!checkInput(gender));

        Gender gender1 = Gender.valueOf(gender);

        do{
        cf = IOManager.getStringInput("Codice Fiscale");
        }while(!checkInput(cf));

        do{
            email = IOManager.getStringInput("Email");
        }while(!checkInput(email));

        do{
            telephoneNumber = IOManager.getStringInput("Numero di telefono");
        }while(!checkInput(telephoneNumber));

        User u = new User(name, surname, gender1, birthDate, cf, email, telephoneNumber);

        return u;
    }

    /**
     * Ritorna un oggetto Booking creato attraverso gli input datosi dall'utente
     * Non viene passato nessun argomento nel metodo
     * L'utente va ad inserire uno a uno gli attributi dell'oggetto Booking
     * @return  Object Booking
     */
    private Booking inputBooking(User user){

       String startDateString = IOManager.getStringInput("Inserisci la data di inizio della prenotazione");
       Date startDate = null;
       try {
           startDate = dateFormatBooking.parse(startDateString);
       } catch (ParseException e) {
           throw new RuntimeException(e);
       }

        String endDateString = IOManager.getStringInput("Inserisci la data di inizio della prenotazione");
        Date endDate = null;
        try {
            endDate = dateFormatBooking.parse(endDateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Booking b = new Booking(user.getId(), startDate, endDate, BookingStatus.PENDING, 0);
        b.setPrice(bookingBusiness.totalCost(b));
        return b;
    }


    /**
     * Controllo stringa se e' null o se e' vuota
     * @param s Stringa da controllare
     * @return  true --> stringa utilizzabile | false --> stringa null o vuota
     */
    private boolean checkInput(String s){
        if(s == null || s.isBlank())
            return false;
        return true;
    }

}
