package com.team.appointment.ui;

import java.util.Scanner;

import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.User;
import com.team.appointment.service.AuthService;
import com.team.appointment.service.SlotService;

public class ConsoleMenu {
    private final Scanner in;
    private final AuthService auth = new AuthService();
    private final SlotService slotService = new SlotService();
    private User currentUser = null;

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
                    handleLogin();
                    break;
                case 2:
                    viewAvailableSlots();
                    break;
                case 3:
                    System.out.println("Book appointment (Sprint 2)");
                    break;
                case 4:
                    System.out.println("Cancel appointment (Sprint 2)");
                    break;
                case 5:
                    handleLogout();
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
        System.out.println("5) Logout");
        System.out.println("0) Exit");
    }

    private void handleLogin() {
        if (currentUser != null) {
            System.out.println("Already logged in as: " + currentUser.getUsername());
            return;
        }
        System.out.print("Username: ");
        String username = in.nextLine().trim();
        System.out.print("Password: ");
        String password = in.nextLine().trim();

        User u = auth.login(username, password);
        if (u == null) {
            System.out.println("❌ Invalid username or password");
        } else {
            currentUser = u;
            System.out.println("✅ Login successful. Welcome " + u.getUsername());
        }
    }

    private void handleLogout() {
        if (currentUser == null) {
            System.out.println("You are not logged in.");
        } else {
            currentUser = null;
            System.out.println("Logged out successfully.");
        }
    }

    private void viewAvailableSlots() {
        System.out.println("Available slots:");
        int i = 1;
        for (AppointmentSlot slot : slotService.getAvailableSlots()) {
            System.out.println(i++ + ") " + slot);
        }
        if (i == 1) {
            System.out.println("No available slots.");
        }
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