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

    private String calculateBirtMonth(String birthDate, Gender gender) {
        String[] months = {"A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T"};
        System.out.println(birthDate);
        System.out.println(birthDate.substring(3,5).trim());
        int month = Integer.parseInt(birthDate.substring(3,5).trim());
        if(gender == Gender.F)
            month += 40;
        return months[month - 1];
    }

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

    public boolean insert(User user){
        if(verifyCf(user) && verifyAge(user)) {
            userRepo.insert(user);
            return true;
        }
        return false;
    }


    public boolean verifyAge(User user){
        LocalDate userBirthDate = user.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int cmp = userBirthDate.compareTo(LocalDate.now());
        return cmp >= MIN_AGE;
    }

    public List<User> findAll(){
        return userRepo.findAll();
    }

    public User findById(int id) {
        return userRepo.findById(id);
    }
}
