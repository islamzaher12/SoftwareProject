package com.team.appointment.ui;

import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;
import com.team.appointment.model.User;
import com.team.appointment.service.AuthService;
import com.team.appointment.service.BookingService;
import com.team.appointment.service.NotificationService;
import com.team.appointment.service.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class MainFrameTest {

    private MainFrame frame;

    private AuthService authService;
    private SlotService slotService;
    private BookingService bookingService;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() throws Exception {
        frame = allocateWithoutConstructor();

        authService = mock(AuthService.class);
        slotService = mock(SlotService.class);
        bookingService = mock(BookingService.class);
        notificationService = mock(NotificationService.class);

        setField(frame, "authService", authService);
        setField(frame, "slotService", slotService);
        setField(frame, "bookingService", bookingService);
        setField(frame, "notificationService", notificationService);

        setField(frame, "cardLayout", new CardLayout());
        setField(frame, "rootPanel", new JPanel(new CardLayout()));

        setField(frame, "loginUsernameField", new JTextField());
        setField(frame, "loginPasswordField", new JPasswordField());
        setField(frame, "loginErrorLabel", new JLabel(" "));

        setField(frame, "headerUserLabel", new JLabel());

        JTabbedPane tabbedPane = new JTabbedPane();
        for (int i = 0; i < 6; i++) {
            tabbedPane.addTab("Tab" + i, new JPanel());
        }
        setField(frame, "tabbedPane", tabbedPane);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"#", "Date", "Time", "Duration", "Participants", "Type", "Status"}, 0);
        setField(frame, "slotsTableModel", model);
        setField(frame, "slotsTable", new JTable(model));
        setField(frame, "showAvailableOnlyCheckBox", new JCheckBox());

        setField(frame, "bookSlotCombo", new JComboBox<String>());
        setField(frame, "bookTypeCombo", new JComboBox<>(AppointmentType.values()));
        setField(frame, "bookParticipantsSpinner", new JSpinner(new SpinnerNumberModel(1, 1, 3, 1)));
        setField(frame, "bookDurationSpinner", new JSpinner(new SpinnerNumberModel(30, 1, 30, 1)));
        setField(frame, "bookStatusLabel", new JLabel(" "));

        setField(frame, "modifyOldSlotCombo", new JComboBox<String>());
        setField(frame, "modifyNewSlotCombo", new JComboBox<String>());
        setField(frame, "modifyTypeCombo", new JComboBox<>(AppointmentType.values()));
        setField(frame, "modifyParticipantsSpinner", new JSpinner(new SpinnerNumberModel(1, 1, 3, 1)));
        setField(frame, "modifyDurationSpinner", new JSpinner(new SpinnerNumberModel(30, 1, 30, 1)));
        setField(frame, "modifyStatusLabel", new JLabel(" "));

        setField(frame, "cancelSlotCombo", new JComboBox<String>());
        setField(frame, "cancelStatusLabel", new JLabel(" "));

        setField(frame, "notificationLogArea", new JTextArea());
        setField(frame, "adminDateField", new JTextField());
        setField(frame, "adminTimeField", new JTextField());
        setField(frame, "adminStatusLabel", new JLabel(" "));
        setField(frame, "adminNewUsernameField", new JTextField());
        setField(frame, "adminNewPasswordField", new JPasswordField());
        setField(frame, "adminIsAdminCheckBox", new JCheckBox());
        setField(frame, "adminUserStatusLabel", new JLabel(" "));
    }

    @Test
    @DisplayName("performLogin: invalid credentials")
    void testPerformLoginInvalid() throws Exception {
        JTextField username = getField("loginUsernameField");
        JPasswordField password = getField("loginPasswordField");
        JLabel error = getField("loginErrorLabel");

        username.setText("wrong");
        password.setText("bad");

        when(authService.login("wrong", "bad")).thenReturn(null);

        invoke("performLogin");

        assertEquals("Invalid username or password.", error.getText());
        assertEquals("", new String(password.getPassword()));
        assertNull(getField("currentUser"));
    }

    @Test
    @DisplayName("setStatus success")
    void testSetStatusSuccess() throws Exception {
        JLabel lbl = new JLabel();

        invoke("setStatus", new Class[]{JLabel.class, String.class, boolean.class}, lbl, "Done", true);

        assertTrue(lbl.getText().contains("Done"));
        assertTrue(lbl.getText().startsWith("✅"));
    }

    @Test
    @DisplayName("setStatus failure")
    void testSetStatusFailure() throws Exception {
        JLabel lbl = new JLabel();

        invoke("setStatus", new Class[]{JLabel.class, String.class, boolean.class}, lbl, "Failed", false);

        assertTrue(lbl.getText().contains("Failed"));
        assertTrue(lbl.getText().startsWith("❌"));
    }

    @Test
    @DisplayName("getAllSlotIndex parses valid combo item")
    void testGetAllSlotIndexValid() throws Exception {
        JComboBox<String> combo = new JComboBox<>();
        combo.addItem("3: 2026-05-01 09:00");

        int result = (int) invoke("getAllSlotIndex", new Class[]{JComboBox.class}, combo);

        assertEquals(3, result);
    }

    @Test
    @DisplayName("getAllSlotIndex handles invalid combo item")
    void testGetAllSlotIndexInvalid() throws Exception {
        JComboBox<String> combo = new JComboBox<>();
        combo.addItem("invalid format");

        int result = (int) invoke("getAllSlotIndex", new Class[]{JComboBox.class}, combo);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("getAllSlotIndex returns 0 when selection is null")
    void testGetAllSlotIndexNull() throws Exception {
        JComboBox<String> combo = new JComboBox<>();

        int result = (int) invoke("getAllSlotIndex", new Class[]{JComboBox.class}, combo);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("refreshSlotsTable with all slots")
    void testRefreshSlotsTableAllSlots() throws Exception {
        JCheckBox checkBox = getField("showAvailableOnlyCheckBox");
        checkBox.setSelected(false);

        AppointmentSlot s1 = mockSlot("2026-05-01", "09:00", true, 30, 2, 3, AppointmentType.values()[0], "CONFIRMED");
        AppointmentSlot s2 = mockSlot("2026-05-01", "10:00", false, 0, 0, 3, null, "AVAILABLE");

        when(slotService.getAllSlots()).thenReturn(Arrays.asList(s1, s2));

        invoke("refreshSlotsTable");

        DefaultTableModel model = getField("slotsTableModel");
        assertEquals(2, model.getRowCount());
        assertEquals("2026-05-01", model.getValueAt(0, 1));
        assertEquals("09:00", model.getValueAt(0, 2));
        assertEquals("30 min", model.getValueAt(0, 3));
        assertEquals("30 min (max)", model.getValueAt(1, 3));
    }

    @Test
    @DisplayName("refreshSlotsTable with available only")
    void testRefreshSlotsTableAvailableOnly() throws Exception {
        JCheckBox checkBox = getField("showAvailableOnlyCheckBox");
        checkBox.setSelected(true);

        AppointmentSlot s1 = mockSlot("2026-05-02", "11:00", false, 0, 0, 3, null, "AVAILABLE");
        when(slotService.getAvailableSlots()).thenReturn(Collections.singletonList(s1));

        invoke("refreshSlotsTable");

        DefaultTableModel model = getField("slotsTableModel");
        assertEquals(1, model.getRowCount());
        assertEquals("2026-05-02", model.getValueAt(0, 1));
    }

    @Test
    @DisplayName("refreshBookCombo populates available slots")
    void testRefreshBookCombo() throws Exception {
        JComboBox<String> combo = getField("bookSlotCombo");

        AppointmentSlot s1 = mockSlot("2026-05-01", "09:00", false, 0, 0, 3, null, "AVAILABLE");
        AppointmentSlot s2 = mockSlot("2026-05-01", "10:00", false, 0, 0, 3, null, "AVAILABLE");
        when(slotService.getAvailableSlots()).thenReturn(Arrays.asList(s1, s2));

        invoke("refreshBookCombo");

        assertEquals(2, combo.getItemCount());
        assertEquals("2026-05-01   09:00", combo.getItemAt(0));
        assertEquals("2026-05-01   10:00", combo.getItemAt(1));
    }

    @Test
    @DisplayName("refreshModifyCombos separates booked and available")
    void testRefreshModifyCombos() throws Exception {
        JComboBox<String> oldCombo = getField("modifyOldSlotCombo");
        JComboBox<String> newCombo = getField("modifyNewSlotCombo");

        AppointmentSlot booked = mockSlot("2026-05-03", "08:00", true, 30, 1, 3, AppointmentType.values()[0], "CONFIRMED");
        AppointmentSlot available = mockSlot("2026-05-03", "09:00", false, 0, 0, 3, null, "AVAILABLE");

        when(slotService.getAllSlots()).thenReturn(Arrays.asList(booked, available));

        invoke("refreshModifyCombos");

        assertEquals(1, oldCombo.getItemCount());
        assertEquals(1, newCombo.getItemCount());
        assertTrue(oldCombo.getItemAt(0).contains("1:"));
        assertTrue(newCombo.getItemAt(0).contains("2:"));
    }

    @Test
    @DisplayName("refreshCancelCombo shows only booked slots")
    void testRefreshCancelCombo() throws Exception {
        JComboBox<String> combo = getField("cancelSlotCombo");

        AppointmentSlot booked = mockSlot("2026-05-03", "08:00", true, 30, 2, 3, AppointmentType.values()[0], "CONFIRMED");
        AppointmentSlot available = mockSlot("2026-05-03", "09:00", false, 0, 0, 3, null, "AVAILABLE");

        when(slotService.getAllSlots()).thenReturn(Arrays.asList(booked, available));

        invoke("refreshCancelCombo");

        assertEquals(1, combo.getItemCount());
        assertTrue(combo.getItemAt(0).contains("2 pax"));
    }

    @Test
    @DisplayName("performBook requires login")
    void testPerformBookRequiresLogin() throws Exception {
        JLabel status = getField("bookStatusLabel");

        invoke("performBook");

        assertTrue(status.getText().contains("Please log in first."));
        verifyNoInteractions(bookingService);
    }

    @Test
    @DisplayName("performBook fails when no available slots")
    void testPerformBookNoSlots() throws Exception {
        setField(frame, "currentUser", regularUser());
        JLabel status = getField("bookStatusLabel");

        invoke("performBook");

        assertTrue(status.getText().contains("No available slots."));
        verifyNoInteractions(bookingService);
    }

    @Test
    @DisplayName("performModify requires login")
    void testPerformModifyRequiresLogin() throws Exception {
        JLabel status = getField("modifyStatusLabel");

        invoke("performModify");

        assertTrue(status.getText().contains("Please log in first."));
    }

    @Test
    @DisplayName("performModify requires old booked slot")
    void testPerformModifyNoOldSlots() throws Exception {
        setField(frame, "currentUser", regularUser());
        JLabel status = getField("modifyStatusLabel");

        invoke("performModify");

        assertTrue(status.getText().contains("No booked slots to modify."));
    }

    @Test
    @DisplayName("performModify requires new available slot")
    void testPerformModifyNoNewSlots() throws Exception {
        setField(frame, "currentUser", regularUser());
        JComboBox<String> oldCombo = getField("modifyOldSlotCombo");
        JLabel status = getField("modifyStatusLabel");

        oldCombo.addItem("1: 2026-05-01 09:00 [TYPE]");

        invoke("performModify");

        assertTrue(status.getText().contains("No available slots to move to."));
    }

    @Test
    @DisplayName("performCancel requires login")
    void testPerformCancelRequiresLogin() throws Exception {
        JLabel status = getField("cancelStatusLabel");

        invoke("performCancel");

        assertTrue(status.getText().contains("Please log in first."));
    }

    @Test
    @DisplayName("performCancel requires booked slots")
    void testPerformCancelNoSlots() throws Exception {
        setField(frame, "currentUser", regularUser());
        JLabel status = getField("cancelStatusLabel");

        invoke("performCancel");

        assertTrue(status.getText().contains("No booked slots available."));
    }

    @Test
    @DisplayName("performCancel failure")
    void testPerformCancelFailure() throws Exception {
        setField(frame, "currentUser", regularUser());

        JComboBox<String> combo = getField("cancelSlotCombo");
        JLabel status = getField("cancelStatusLabel");
        combo.addItem("2: 2026-05-01 09:00 [2 pax | TYPE]");
        combo.setSelectedIndex(0);

        when(bookingService.cancelBooking(2)).thenReturn(false);

        invoke("performCancel");

        assertTrue(status.getText().contains("Cancellation failed."));
    }

    @Test
    @DisplayName("performAddUser denied for non-admin")
    void testPerformAddUserDenied() throws Exception {
        setField(frame, "currentUser", regularUser());
        JLabel status = getField("adminUserStatusLabel");

        invoke("performAddUser");

        assertTrue(status.getText().contains("Access denied"));
        verifyNoInteractions(authService);
    }

    @Test
    @DisplayName("performAddUser empty username")
    void testPerformAddUserEmptyUsername() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField username = getField("adminNewUsernameField");
        JPasswordField password = getField("adminNewPasswordField");
        JLabel status = getField("adminUserStatusLabel");

        username.setText("");
        password.setText("1234");

        invoke("performAddUser");

        assertTrue(status.getText().contains("Username cannot be empty."));
    }

    @Test
    @DisplayName("performAddUser empty password")
    void testPerformAddUserEmptyPassword() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField username = getField("adminNewUsernameField");
        JPasswordField password = getField("adminNewPasswordField");
        JLabel status = getField("adminUserStatusLabel");

        username.setText("newuser");
        password.setText("");

        invoke("performAddUser");

        assertTrue(status.getText().contains("Password cannot be empty."));
    }

    @Test
    @DisplayName("performAddUser success")
    void testPerformAddUserSuccess() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField username = getField("adminNewUsernameField");
        JPasswordField password = getField("adminNewPasswordField");
        JCheckBox adminCheck = getField("adminIsAdminCheckBox");
        JLabel status = getField("adminUserStatusLabel");

        username.setText("newuser");
        password.setText("1234");
        adminCheck.setSelected(true);

        when(authService.addUser("newuser", "1234", true)).thenReturn(true);

        invoke("performAddUser");

        assertTrue(status.getText().contains("User 'newuser' added successfully."));
        assertEquals("", username.getText());
        assertEquals("", new String(password.getPassword()));
        assertFalse(adminCheck.isSelected());
    }

    @Test
    @DisplayName("performAddUser duplicate username")
    void testPerformAddUserDuplicate() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField username = getField("adminNewUsernameField");
        JPasswordField password = getField("adminNewPasswordField");
        JCheckBox adminCheck = getField("adminIsAdminCheckBox");
        JLabel status = getField("adminUserStatusLabel");

        username.setText("existing");
        password.setText("1234");
        adminCheck.setSelected(false);

        when(authService.addUser("existing", "1234", false)).thenReturn(false);

        invoke("performAddUser");

        assertTrue(status.getText().contains("already exists"));
    }

    @Test
    @DisplayName("performAddSlot denied for non-admin")
    void testPerformAddSlotDenied() throws Exception {
        setField(frame, "currentUser", regularUser());
        JLabel status = getField("adminStatusLabel");

        invoke("performAddSlot");

        assertTrue(status.getText().contains("Access denied"));
        verify(slotService, never()).addSlot(anyString(), anyString(), anyInt());
    }

    @Test
    @DisplayName("performAddSlot empty fields")
    void testPerformAddSlotEmptyFields() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField date = getField("adminDateField");
        JTextField time = getField("adminTimeField");
        JLabel status = getField("adminStatusLabel");

        date.setText("");
        time.setText("");

        invoke("performAddSlot");

        assertTrue(status.getText().contains("Please enter both date and time."));
    }

    @Test
    @DisplayName("performAddSlot invalid date")
    void testPerformAddSlotInvalidDate() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField date = getField("adminDateField");
        JTextField time = getField("adminTimeField");
        JLabel status = getField("adminStatusLabel");

        date.setText("01-05-2026");
        time.setText("09:00");

        invoke("performAddSlot");

        assertTrue(status.getText().contains("Date must be in YYYY-MM-DD format."));
    }

    @Test
    @DisplayName("performAddSlot invalid time")
    void testPerformAddSlotInvalidTime() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField date = getField("adminDateField");
        JTextField time = getField("adminTimeField");
        JLabel status = getField("adminStatusLabel");

        date.setText("2026-05-01");
        time.setText("9 AM");

        invoke("performAddSlot");

        assertTrue(status.getText().contains("Time must be in HH:mm format."));
    }

    @Test
    @DisplayName("performAddSlot success")
    void testPerformAddSlotSuccess() throws Exception {
        setField(frame, "currentUser", adminUser());

        JTextField date = getField("adminDateField");
        JTextField time = getField("adminTimeField");
        JLabel status = getField("adminStatusLabel");

        date.setText("2026-05-01");
        time.setText("09:00");

        when(slotService.getAllSlots()).thenReturn(Collections.emptyList());
        when(slotService.getAvailableSlots()).thenReturn(Collections.emptyList());

        invoke("performAddSlot");

        verify(slotService).addSlot("2026-05-01", "09:00", 30);
        assertTrue(status.getText().contains("Slot added: 2026-05-01 at 09:00"));
    }

    private AppointmentSlot mockSlot(String date, String time, boolean booked,
                                     int duration, int participants, int maxParticipants,
                                     AppointmentType type, String status) {
        AppointmentSlot slot = mock(AppointmentSlot.class);
        when(slot.getDate()).thenReturn(date);
        when(slot.getTime()).thenReturn(time);
        when(slot.isBooked()).thenReturn(booked);
        when(slot.getDuration()).thenReturn(duration);
        when(slot.getParticipantCount()).thenReturn(participants);
        when(slot.getMaxParticipants()).thenReturn(maxParticipants);
        when(slot.getType()).thenReturn(type);
        when(slot.getStatus()).thenReturn(status);
        return slot;
    }

    private User regularUser() {
        User user = mock(User.class);
        when(user.isAdmin()).thenReturn(false);
        when(user.getUsername()).thenReturn("user");
        return user;
    }

    private User adminUser() {
        User user = mock(User.class);
        when(user.isAdmin()).thenReturn(true);
        when(user.getUsername()).thenReturn("admin");
        return user;
    }

    private void invoke(String methodName) throws Exception {
        Method m = MainFrame.class.getDeclaredMethod(methodName);
        m.setAccessible(true);
        m.invoke(frame);
    }

    private Object invoke(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method m = MainFrame.class.getDeclaredMethod(methodName, paramTypes);
        m.setAccessible(true);
        return m.invoke(frame, args);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = MainFrame.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private <T> T getField(String fieldName) throws Exception {
        Field field = MainFrame.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(frame);
    }

    private MainFrame allocateWithoutConstructor() throws Exception {
        Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
        return (MainFrame) unsafe.allocateInstance(MainFrame.class);
    }
}