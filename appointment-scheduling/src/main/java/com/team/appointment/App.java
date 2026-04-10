package com.team.appointment;

import com.team.appointment.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point for the Appointment Scheduling System.
 * Launches the Swing GUI on the Event Dispatch Thread using the system
 * look-and-feel for the host platform.
 *
 * @author Team
 * @version 1.0
 */
public class App {

    /**
     * Main method – sets the look-and-feel and opens the {@link MainFrame}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Use system look-and-feel for a native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to default Swing L&F silently
        }

        SwingUtilities.invokeLater(MainFrame::new);
    }
}
