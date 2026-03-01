package com.team.appointment.ui;

import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner in;

    public ConsoleMenu(Scanner in) {
        this.in = in;
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Choose: ");

            switch (choice) {
            case 1:
                System.out.println("Login (TODO)");
                break;

            case 2:
                System.out.println("View available slots (TODO)");
                break;

            case 3:
                System.out.println("Book appointment (TODO)");
                break;

            case 4:
                System.out.println("Cancel appointment (TODO)");
                break;

            case 0:
                running = false;
                break;

            default:
                System.out.println("Invalid choice. Try again.");
        }
            System.out.println();
        }

        System.out.println("Bye!");
    }

    private void printMainMenu() {
        System.out.println("=== Appointment Scheduling System ===");
        System.out.println("1) Login");
        System.out.println("2) View available slots");
        System.out.println("3) Book appointment");
        System.out.println("4) Cancel appointment");
        System.out.println("0) Exit");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}