package com.team.appointment;

import java.util.Scanner;
import com.team.appointment.ui.ConsoleMenu;

public class App {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ConsoleMenu menu = new ConsoleMenu(in);
        menu.start();
        in.close();
    }
}