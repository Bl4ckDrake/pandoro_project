package it.voltats.gestionepista.presentation;

import it.voltats.gestionepista.Main;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.entity.model.Gender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class MenuManagement {
    BookingRepoImpl bookingRepo;
    UserRepoImpl userRepo;
    Scanner scanner;
    UserBusiness userBusiness;
    SimpleDateFormat dateFormat;

    public MenuManagement() {
        this.bookingRepo = new BookingRepoImpl();
        this.userRepo = new UserRepoImpl();
        this.userBusiness = new UserBusiness();
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy");
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


            switch (choice){
                case -1:
                    break;

                case 0:

                    break;
                case 1:
                    bookingRepo.update(null);
                    System.out.println();
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

                case 3:
                    bookingRepo.delete(null);
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
                    System.out.println("Arrivederci!!!");
                    break;
            }


            System.out.println("\n\n");

        }while(choice != 5);

    }
    
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

        System.out.println("Codice Fiscale: ");
        cf = scanner.nextLine().trim();

        System.out.println("Email: ");
        email = scanner.nextLine().trim();

        System.out.println("Numero di telefono: ");
        telephoneNumber = scanner.nextLine().trim();

        return new User(name, surname, gender1, null, cf, email, telephoneNumber);
    }

    private boolean checkInput(String s){
        if(s == null || s.isBlank())
            return false;
        return true;
    }
    private Booking getRandomBooking(){
        Random rand = new Random();

        int uid = rand.nextInt(1000);
        int currentYear = java.time.LocalDate.now().getYear();

        int startH = rand.nextInt(15)+8;
        int endH = startH + rand.nextInt(8) + 1;
        int endM = 0;
        int endS = 0;

        if(endH > 24) {
            endH = 24;
        }

        Date start = new Date(currentYear-1900,1,1, startH ,0 ,0);
        Date end = new Date(currentYear-1900,1,1, endH ,endM ,endS);
        //BookingStatus status = BookingStatus.values()[rand.nextInt(BookingStatus.values().length)];

        return new Booking(uid, start, end, BookingStatus.PENDING, 0);
    }
}
