package it.voltats.gestionepista.presentation;
import java.util.Scanner;

public class IOManagement {
    private Scanner scanner;

    public IOManagement() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Ritorna un valore intero
     * Prende in input un valore String e lo converte opportunamente in int
     * @param prompt String di output visualizzazione
     * @return  intero convertito da Stringa
     */
    public int getIntInput(String prompt) {
        int input = 0;
        boolean validInput = false;

        do {
            try {
                System.out.print("[+] - " + prompt + ": ");
                input = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.err.println("[-] - Inserisci un numero valido.");
            }
        } while (!validInput);

        return input;
    }

    /**
     * Ritorna un valore stringa
     * Prende in input una Stringa e la ritorna
     * @param prompt String di output visualizzazione
     * @return  Stringa messa in input
     */
    public String getStringInput(String prompt) {
        System.out.print("[+] - " + prompt + ": ");
        return scanner.nextLine();
    }

    public void output(String message) {
        System.out.println("[+] - " + message);
    }
}