package com.team.appointment.ui;

import com.team.appointment.service.EmailNotifier;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

      
        EmailNotifier notifier = new EmailNotifier();
        notifier.send("Test email from main App ");
        
        
    البرنامالأساسي
        Scanner in = new Scanner(System.in);
        ConsoleMenu menu = new ConsoleMenu(in);
        menu.start();
        in.close();
    }
}