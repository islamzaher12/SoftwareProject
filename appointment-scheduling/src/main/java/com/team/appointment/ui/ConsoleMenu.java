package com.team.appointment.ui;
import com.team.appointment.service.EmailNotifier;

import java.util.Scanner;
import com.team.appointment.service.BookingService;

import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.User;
import com.team.appointment.service.AuthService;
import com.team.appointment.service.SlotService;
import com.team.appointment.service.ConsoleNotifier;
import com.team.appointment.service.NotificationService;

public class ConsoleMenu {
	
    private final Scanner in;
    private final AuthService auth = new AuthService();
    private final SlotService slotService = new SlotService();
    private final BookingService bookingService = new BookingService(slotService);
    private final NotificationService notificationService = new NotificationService(slotService, new EmailNotifier());
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
                    bookAppointment();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    modifyAppointment();
                    break;
                case 6:
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
     
        System.out.println("5) Modify appointment");
        System.out.println("6) Logout");
       
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
    private void bookAppointment() {
    	if (currentUser == null) {
    	    System.out.println("Please login first.");
    	    return;
    	}

        System.out.println("Available slots:");

        int i = 1;

        for (AppointmentSlot slot : slotService.getAvailableSlots()) {
            System.out.println(i++ + ") " + slot);
        }

        if (i == 1) {
            System.out.println("No available slots.");
            return;
        }

        int slotNumber = readInt("Choose slot number: ");

        int duration = readInt("Enter duration (minutes): ");

        int participants = readInt("Enter number of participants: ");

        boolean booked = bookingService.bookAppointment(slotNumber, duration, participants);

        if (booked) {
            System.out.println("Appointment booked successfully.");
            notificationService.sendReminders();
        } else {
            System.out.println("Invalid booking (slot, duration, or participants).");
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
    private void modifyAppointment() {
        if (currentUser == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.println("All slots:");
        int i = 1;
        for (AppointmentSlot slot : slotService.getAllSlots()) {
            System.out.println(i++ + ") " + slot);
        }

        int oldSlotNumber = readInt("Choose your booked slot to modify: ");

        System.out.println("Available slots:");
        int j = 1;
        for (AppointmentSlot slot : slotService.getAvailableSlots()) {
            System.out.println(j++ + ") " + slot);
        }

        if (j == 1) {
            System.out.println("No available slots.");
            return;
        }

        int newSlotNumber = readInt("Choose new slot number: ");
        int duration = readInt("Enter duration (minutes): ");
        int participants = readInt("Enter number of participants: ");

        boolean modified = bookingService.modifyBooking(oldSlotNumber, newSlotNumber, duration, participants);

        if (modified) {
            System.out.println("Appointment modified successfully.");
            notificationService.sendReminders();
        } else {
            System.out.println("Modification failed. Check slot numbers, future appointment rule, or booking details.");
        }
    }
    private void cancelAppointment() {
    	
    	if (currentUser == null) {
    	    System.out.println("Please login first.");
    	    return;
    	} 
    	
        int i = 1;

        for (AppointmentSlot slot : slotService.getAllSlots()) {
            System.out.println(i++ + ") " + slot);
        }

        int slotNumber = readInt("Choose slot number to cancel: ");

        boolean canceled = bookingService.cancelBooking(slotNumber);

        if (canceled) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Invalid slot number or slot is not booked.");
        }
    }
}