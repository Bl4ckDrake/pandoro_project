package it.voltats.gestionepista.business;

import it.voltats.gestionepista.db.entity.Booking;
import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.Gender;
import it.voltats.gestionepista.db.impl.UserRepoImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

public class UserBusiness {

    private static final String CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final String VOWELS = "AEIOU";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
    private static final int MIN_AGE = 18;
    private static final UserRepoImpl userRepo = new UserRepoImpl();

    /**
     * Verifica codice fiscale
     * Richiama i metodi di calculareCf, calculateControlChar, calculateBirthMonth ,extractConsonants
     * @param user Utente da cui controllare il suo codice fiscale
     * @return true --> cf verificato | false --> cf falso
     */
    public boolean verifyCf(User user){
        String cf = user.getCf();
        if(cf == null || cf.length() != 16)
            return false;

        cf = cf.toUpperCase();

        if(!cf.substring(0,11).equals(calculateCf(user.getName(), user.getSurname(),
                user.getGender(), new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()))))
            return false;
        return(cf.substring(15).equals(calculateControlChar(cf)) );
    }

    /**
     * Calcola un codice fiscale dato i parametri informazioni sulla persona
     * @param name  nome della persona
     * @param surname   cognome della persona
     * @param gender    genere della persona
     * @param birthDate compleanno della persona
     * @return  Stringa con il codice fiscale calcolato
     */
    private String calculateCf(String name, String surname, Gender gender,
                              String birthDate){
        name = name.toUpperCase();
        surname = surname.toUpperCase();
        String consonantsSurname = extractConsonants(surname);
        String consonantsName = extractConsonants(name);
        String birthYear= birthDate.substring(8, 10);
        String birthMonth = calculateBirthMonth(birthDate, gender);
        String birthDay = birthDate.substring(0,2);

        return consonantsSurname + consonantsName + birthYear + birthMonth + birthDay;
    }

    /**
     * Calcola il carattere di controllo finale del cf
     * @param input Codice fiscale
     * @return carattere controllo
     */
    private String calculateControlChar(String input){
        int[] evenCharWeight = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23};

        int sum = 0;
        for(int i = 0; i < input.length() - 1; i++){
            char c = input.charAt(i);
            int value;
            value = (Character.isLetter(c)) ? ALPHABET.indexOf(c) : c - 48;
            sum += ((i + 1) % 2 == 0) ? value : evenCharWeight[value];
        }
        int remainder = sum % 26;
        return String.valueOf(ALPHABET.charAt(remainder));
    }

    /**
     * Calcola il carattere in base a Mese del compleanno e genere
     * @param birthDate compleanno utente
     * @param gender    genere utente
     * @return Stringa con il carattere del mese
     */
    private String calculateBirthMonth(String birthDate, Gender gender) {
        String[] months = {"A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T"};
        int month = Integer.parseInt(birthDate.substring(3,5).trim());
        return months[month - 1];
    }

    /**
     * Estrae le consonanti da una Stringa
     * @param input stringa da cui estrappolare le consonanti
     * @return Stringa di consonanti
     */
    private String extractConsonants(String input) {
        StringBuilder consonants = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isLetter(c) && CONSONANTS.contains(String.valueOf(c))) {
                consonants.append(c);
                if (consonants.length() == 3)
                    break;
            }
        }
        if (consonants.length() < 3)
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (Character.isLetter(c) && VOWELS.contains(String.valueOf(c))) {
                    consonants.append(c);
                    if (consonants.length() == 3)
                        break;
                }
            }
        //se il nome o cognome non contengono abbastanza lettere si devono aggiungere delle X
        while(consonants.length() < 3)
            consonants.append("X");

        return consonants.toString();
    }

    /**
     * Aggiunta al database di un utente
     * Prima viene effettuato controllo di codice fiscale ed eta'
     * @param user
     * @return true --> Inserito correttamente | false --> cf falso e/o eta' troppo piccola
     */
    public boolean insert(User user){
        if(verifyCf(user) && verifyAge(user)) {
            userRepo.insert(user);
            return true;
        }
        return false;
    }


    /**
     * Verifica eta'
     * @param user Utente da cui controllare l'eta'
     * @return true --> maggiorenne | false --> minorenne
     */
    public boolean verifyAge(User user){
        LocalDate userBirthDate = user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int cmp = LocalDate.now().compareTo(userBirthDate);
        return cmp >= MIN_AGE;
    }

    /**
     * @return lista di utenti
     */
    public List<User> findAll(){
        return userRepo.findAll();
    }

    /**
     * Ritorna Utente dato da un ID
     * @param id ID da cercare
     * @return Utente trovato per ID
     */
    public User findById(int id) {
        return userRepo.findById(id);
    }
}
