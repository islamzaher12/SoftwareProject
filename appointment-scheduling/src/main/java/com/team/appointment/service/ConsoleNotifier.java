package com.team.appointment.service;

public class ConsoleNotifier implements Notifier {

    @Override
    public void send(String message) {
        System.out.println("Notification sent: " + message);
    }
}