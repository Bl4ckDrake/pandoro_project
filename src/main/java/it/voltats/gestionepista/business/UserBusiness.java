package it.voltats.gestionepista.business;

import it.voltats.gestionepista.db.entity.User;
import it.voltats.gestionepista.db.entity.model.Gender;

public class UserBusiness {

    private static final String CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final String VOCALS = "AEIOU";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVXYZ";

    public boolean verifyCf(User user){
        String cf = user.getCf();
        if(cf == null || cf.length() != 16)
            return false;

        cf = cf.toUpperCase();

        if(!cf.substring(0,11).equals(calculateCf(user.getName(), user.getSurname(),
                user.getGender(), user.getBirthdate().toString())))
            return false;
        else
        if(cf.substring(15).equals(calculateControlChar(cf)) )
            return true;


        return false;
        //verifica
    }

    private String calculateCf(String name, String surname, Gender gender,
                              String birthDate){
        name = name.toUpperCase();
        surname = surname.toUpperCase();
        String consonantsSurname = extractConsonants(surname);
        String consonantsName = extractConsonants(name);
        String birthYear= birthDate.substring(6, 8);
        String birthMonth = calculateBirtMonth(birthDate, gender);
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
        System.out.println(String.valueOf(ALPHABET.charAt(remainder)));
        return String.valueOf(ALPHABET.charAt(remainder));
    }

    private String calculateBirtMonth(String birthDate, Gender gender) {
        String[] months = {"A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T"};
        int month = Integer.parseInt(birthDate.substring(3,5));
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
                if (Character.isLetter(c) && VOCALS.contains(String.valueOf(c))) {
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
}
