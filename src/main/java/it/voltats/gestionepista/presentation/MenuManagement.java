package it.voltats.gestionepista.presentation;

import it.voltats.gestionepista.Main;
import it.voltats.gestionepista.business.UserBusiness;
import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.BookingStatus;
import it.voltats.gestionepista.db.entity.model.Gender;
import it.voltats.gestionepista.db.impl.BookingRepoImpl;
import it.voltats.gestionepista.db.impl.UserRepoImpl;

import java.io.IOException;
import java.text.DateFormat;
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
        int choice = 0;
        do{
            System.out.println("[0] --> Inserisci una nuova prenotazione");
            System.out.println("[1] --> Modifica lo stato di pagamento di una prenotazione");
            System.out.println("[2] --> Rimuovi una prenotazione");
            System.out.println("[3] --> Cerca una prenotazione per ID");
            System.out.println("[4] --> Cerca le prenotazioni di una giornata");
            System.out.println("[5] --> Esci");
            System.out.println("[6] --> genera prenotazione");
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

                case 2:

                    //System.out.println(userBusiness.verifyCf(user));
                    break;

                case 3:
                    bookingRepo.delete(null);
                    break;

                case 4:
                    bookingRepo.findAllByUserId(0);
                    break;

                case 6:
                    bookingRepo.insert(new MenuManagement().getRandomBooking());
                    break;
            }

            for(Booking b: bookingRepo.findAll()){
                System.out.println("ID: "+ b.getId() + " - UID: " + b.getUserId() + " - DATE: " + b.getStartDate()
                        + " - " + b.getEndDate() + " - STATUS: " + b.getStatus());
            }

        }while(choice != 5);

    }
    
    private User inputUser(){
        System.out.println("Inserimento dati cliente:");
        String name, surname, birthDateString, gender, cf, email, telephoneNumber;

        do{
            System.out.println("Nome: ");
            name = scanner.nextLine().trim();
        }while(!checkInput(name));

        do {
            System.out.println("Cognome: ");
            surname = scanner.nextLine().trim();
        }while(!checkInput(surname));

        do{
            System.out.println("Data di nascita: ");
            birthDateString = scanner.nextLine().trim();
            Date birthDate = null;
            try {
                birthDate = dateFormat.parse(birthDateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }while(!checkInput(birthDateString));


        System.out.println("Genere (M o F): ");
        gender = scanner.nextLine().trim();
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

        int startH = rand.nextInt(16)+8;
        int endH = startH - rand.nextInt(8) + 1;

        if(endH >= 24)
            endH = 23;

        Date start = new Date(currentYear-1900,1,1, startH ,0 ,0);
        Date end = new Date(currentYear-1900,1,1, endH ,0 ,0);
        BookingStatus status = BookingStatus.values()[rand.nextInt(BookingStatus.values().length)];

        return new Booking(uid, start, end, status);
    }
}
