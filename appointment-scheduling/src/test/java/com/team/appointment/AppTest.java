package com.team.appointment;

import com.team.appointment.ui.MainFrame;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;

public class AppTest {

    @Test
    void testMainLaunchesMainFrame() throws Exception {
        try (MockedConstruction<MainFrame> mocked = mockConstruction(MainFrame.class)) {

            assertDoesNotThrow(() -> App.main(new String[]{}));

            // ننتظر تنفيذ invokeLater على الـ EDT
            SwingUtilities.invokeAndWait(() -> {});

            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void testMainWithArgumentsStillLaunchesMainFrame() throws Exception {
        try (MockedConstruction<MainFrame> mocked = mockConstruction(MainFrame.class)) {

            assertDoesNotThrow(() -> App.main(new String[]{"arg1", "arg2"}));

            SwingUtilities.invokeAndWait(() -> {});

            assertEquals(1, mocked.constructed().size());
        }
    }
}