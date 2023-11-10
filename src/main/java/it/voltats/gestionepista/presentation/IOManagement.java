package it.voltats.gestionepista.presentation;
import java.util.Scanner;

public class IOManagement {
    private Scanner scanner;

    public IOManagement() {
        this.scanner = new Scanner(System.in);
    }

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

    public String getStringInput(String prompt) {
        System.out.print("[+] - " + prompt + ": ");
        return scanner.nextLine();
    }

    public void output(String message) {
        System.out.println("[+] - " + message);
    }
}