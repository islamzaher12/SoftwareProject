package com.team.appointment.ui;

import com.team.appointment.model.AppointmentSlot;
import com.team.appointment.model.AppointmentType;
import com.team.appointment.model.User;
import com.team.appointment.service.AuthService;
import com.team.appointment.service.BookingService;
import com.team.appointment.service.GUINotifier;
import com.team.appointment.service.NotificationService;
import com.team.appointment.service.SlotService;
import com.team.appointment.service.EmailNotifier;
import com.team.appointment.service.CompositeNotifier;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Main application window for the Appointment Scheduling System.
 * Provides a tabbed GUI for booking, modifying, cancelling appointments,
 * viewing notifications, and admin slot management.
 *
 * @author Team
 * @version 1.0
 */
public class MainFrame extends JFrame {

    private static final Color PRIMARY      = new Color(15, 52, 96);
    private static final Color SECONDARY    = new Color(21, 101, 192);
    private static final Color ACCENT       = new Color(16, 185, 129);
    private static final Color DANGER       = new Color(220, 53, 69);
    private static final Color BG           = new Color(240, 245, 255);
    private static final Color SURFACE      = Color.WHITE;
    private static final Color TEXT_DARK    = new Color(15, 23, 42);
    private static final Color TEXT_LIGHT   = new Color(100, 116, 139);
    private static final Color TABLE_HEADER = new Color(30, 58, 138);

    private static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  26);
    private static final Font FONT_H2     = new Font("Segoe UI", Font.BOLD,  16);
    private static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BTN    = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font FONT_LABEL  = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font FONT_MONO   = new Font("Consolas",  Font.PLAIN, 12);

    private static final int FIELD_WIDTH  = 520;
    private static final int FIELD_HEIGHT = 42;
    private static final int LABEL_WIDTH  = 200;

    private final AuthService         authService    = new AuthService();
    private final SlotService         slotService    = new SlotService();
    private final BookingService      bookingService = new BookingService(slotService);
    private       GUINotifier         guiNotifier;
    private       NotificationService notificationService;

    private User currentUser = null;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel     rootPanel  = new JPanel(cardLayout);

    // Login
    private JTextField     loginUsernameField;
    private JPasswordField loginPasswordField;
    private JLabel         loginErrorLabel;

    // Header
    private JLabel      headerUserLabel;
    private JTabbedPane tabbedPane;

    // View Slots
    private DefaultTableModel slotsTableModel;
    private JTable            slotsTable;
    private JCheckBox         showAvailableOnlyCheckBox;

    // Book
    private JComboBox<String>          bookSlotCombo;
    private JComboBox<AppointmentType> bookTypeCombo;
    private JSpinner                   bookParticipantsSpinner;
    private JLabel                     bookStatusLabel;

    // Modify
    private JComboBox<String>          modifyOldSlotCombo;
    private JComboBox<String>          modifyNewSlotCombo;
    private JComboBox<AppointmentType> modifyTypeCombo;
    private JSpinner                   modifyParticipantsSpinner;
    private JLabel                     modifyStatusLabel;

    // Cancel
    private JComboBox<String> cancelSlotCombo;
    private JLabel            cancelStatusLabel;

    // Notifications / Admin
    private JTextArea  notificationLogArea;
    private JTextField adminDateField;
    
    private JTextField adminTimeField;
    private JLabel     adminStatusLabel;
    private JSpinner                   bookDurationSpinner;

    private JSpinner                   modifyDurationSpinner;

    private JTextField                 adminNewUsernameField;
    private JPasswordField             adminNewPasswordField;
    private JCheckBox                  adminIsAdminCheckBox;
    private JLabel                     adminUserStatusLabel;


    public MainFrame() {
        super("Appointment Scheduling System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        rootPanel.add(buildLoginPanel(),     "LOGIN");
        rootPanel.add(buildDashboardPanel(), "DASHBOARD");

        add(rootPanel);
        cardLayout.show(rootPanel, "LOGIN");
        setVisible(true);
    }

   
    private JPanel buildLoginPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(PRIMARY);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 220, 255), 1, true),
                new EmptyBorder(40, 50, 40, 50)));
        card.setMaximumSize(new Dimension(440, 520));

        JLabel iconLabel = new JLabel("\uD83D\uDCC5", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Appointment Scheduler", SwingConstants.CENTER);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Please sign in to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(FONT_BODY);
        subtitleLabel.setForeground(TEXT_LIGHT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = styledLabel("Username");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginUsernameField = styledTextField("admin");
        loginUsernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginUsernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel passwordLabel = styledLabel("Password");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginPasswordField = new JPasswordField();
        styleTextField(loginPasswordField, "");
        loginPasswordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        loginErrorLabel = new JLabel(" ");
        loginErrorLabel.setFont(FONT_SMALL);
        loginErrorLabel.setForeground(DANGER);
        loginErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = primaryButton("  Sign In  ");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        JLabel hint = new JLabel("<html><center><font color='#94a3b8'>Demo: admin/1234 &nbsp;|&nbsp; user/1111</font></center></html>");
        hint.setFont(FONT_SMALL);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(28));
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(loginUsernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(loginPasswordField);
        card.add(Box.createVerticalStrut(8));
        card.add(loginErrorLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(16));
        card.add(hint);

        ActionListener doLogin = e -> performLogin();
        loginBtn.addActionListener(doLogin);
        loginPasswordField.addActionListener(doLogin);
        loginUsernameField.addActionListener(doLogin);

        outer.add(card);
        return outer;
    }

    private void performLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();
        User u = authService.login(username, password);
        if (u == null) {
            loginErrorLabel.setText("Invalid username or password.");
            loginPasswordField.setText("");
        } else {
            loginErrorLabel.setText(" ");
            currentUser = u;
            onLoginSuccess();
        }
    }
    private JPanel buildDashboardPanel() {
        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(BG);
        dashboard.add(buildHeaderBar(),  BorderLayout.NORTH);
        dashboard.add(buildTabbedPane(), BorderLayout.CENTER);
        dashboard.add(buildStatusBar(),  BorderLayout.SOUTH);
        return dashboard;
    }

    /**
     * Builds the top header bar with app name, user info, and logout button.
     *
     * @return the header panel
     */
    private JPanel buildHeaderBar() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel appNameLabel = new JLabel("\uD83D\uDCC5  Appointment Scheduling System");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        appNameLabel.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);

        headerUserLabel = new JLabel();
        headerUserLabel.setFont(FONT_BODY);
        headerUserLabel.setForeground(new Color(186, 230, 253));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(FONT_BTN);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(DANGER);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(new EmptyBorder(8, 18, 8, 18));
        logoutBtn.addActionListener(e -> performLogout());

        rightPanel.add(headerUserLabel);
        rightPanel.add(logoutBtn);
        header.add(appNameLabel, BorderLayout.WEST);
        header.add(rightPanel,   BorderLayout.EAST);
        return header;
    }

    /**
     * Builds the tabbed pane with all functional tabs.
     *
     * @return the configured JTabbedPane
     */
    private JTabbedPane buildTabbedPane() {
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(BG);
        tabbedPane.setForeground(PRIMARY);

        tabbedPane.addTab("  View Slots  ",    buildViewSlotsPanel());
        tabbedPane.addTab("  Book  ",          buildBookPanel());
        tabbedPane.addTab("  Modify  ",        buildModifyPanel());
        tabbedPane.addTab("  Cancel  ",        buildCancelPanel());
        tabbedPane.addTab("  Notifications  ", buildNotificationsPanel());
        tabbedPane.addTab("  Admin Panel  ",   buildAdminPanel());
        return tabbedPane;
    }

    /**
     * Builds the bottom status bar.
     *
     * @return the status bar panel
     */
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        bar.setBackground(new Color(226, 232, 240));
        JLabel lbl = new JLabel("Appointment Scheduling System  v1.0  |  Team Project");
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(TEXT_LIGHT);
        bar.add(lbl);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tab 1 – View Slots
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Builds the "View Appointment Slots" panel.
     *
     * @return the panel
     */
    private JPanel buildViewSlotsPanel() {
        JPanel panel = contentPanel("View Appointment Slots");

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        toolbar.setOpaque(false);

        showAvailableOnlyCheckBox = new JCheckBox("Show available only");
        showAvailableOnlyCheckBox.setFont(FONT_BODY);
        showAvailableOnlyCheckBox.setOpaque(false);
        showAvailableOnlyCheckBox.addActionListener(e -> refreshSlotsTable());

        JButton refreshBtn = accentButton("Refresh");
        refreshBtn.addActionListener(e -> refreshSlotsTable());

        toolbar.add(showAvailableOnlyCheckBox);
        toolbar.add(refreshBtn);

        String[] columns = {"#", "Date", "Time", "Duration", "Participants", "Type", "Status"};
        slotsTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        slotsTable = new JTable(slotsTableModel);
        styleTable(slotsTable);

        JScrollPane scrollPane = new JScrollPane(slotsTable);
        scrollPane.setBorder(new LineBorder(new Color(200, 215, 240), 1));

        panel.add(toolbar,    BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /** Repopulates the slots table from the current slot service data. */
    private void refreshSlotsTable() {
        slotsTableModel.setRowCount(0);
        List<AppointmentSlot> slots = showAvailableOnlyCheckBox.isSelected()
                ? slotService.getAvailableSlots()
                : slotService.getAllSlots();
        int i = 1;
        for (AppointmentSlot s : slots) {
            String typeStr = (s.getType() != null) ? s.getType().name() : "-";
            String dur = s.isBooked() ? s.getDuration() + " min" : "30 min (max)";
            slotsTableModel.addRow(new Object[]{
                    i++, s.getDate(), s.getTime(),
                    dur,
                    s.getParticipantCount() + "/" + s.getMaxParticipants(),
                    typeStr, s.getStatus()
            });
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tab 2 – Book Appointment
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Builds the "Book an Appointment" panel.
     *
     * @return the panel
     */
    private JPanel buildBookPanel() {
        JPanel panel = contentPanel("Book an Appointment");

        JPanel form = buildFormContainer();

        bookSlotCombo           = new JComboBox<>();
        bookTypeCombo           = new JComboBox<>(AppointmentType.values());
        bookParticipantsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        bookDurationSpinner     = new JSpinner(new SpinnerNumberModel(30, 1, 30, 1));
        bookStatusLabel         = statusLabel();

        JButton bookBtn = primaryButton("  Book Appointment  ");
        bookBtn.addActionListener(e -> performBook());

        addRow(form, 0, "Available Slot :",      bookSlotCombo);
        addRow(form, 1, "Appointment Type :",    bookTypeCombo);
        addRow(form, 2, "Participants (max 3):", bookParticipantsSpinner);
        addRow(form, 3, "Duration (min) :",      bookDurationSpinner);
        addButtonRow(form, 4, bookBtn, bookStatusLabel);

        panel.add(new JScrollPane(form, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return panel;
    }

    /** Updates the participant spinner maximum to match the selected appointment type. */
    private void updateBookParticipantMax() {
        AppointmentType t = (AppointmentType) bookTypeCombo.getSelectedItem();
        if (t == null) return;
        int max = com.team.appointment.service.TypeRule.maxForType(t);
        ((SpinnerNumberModel) bookParticipantsSpinner.getModel()).setMaximum(max);
        int cur = (Integer) bookParticipantsSpinner.getValue();
        if (cur > max) bookParticipantsSpinner.setValue(max);
    }

    /**
     * Executes the booking action from the form inputs.
     * Shows an error dialog if participants exceed 3, or a confirmation
     * dialog on success.
     */
    private void performBook() {
        if (currentUser == null) {
            setStatus(bookStatusLabel, "Please log in first.", false);
            return;
        }
        if (bookSlotCombo.getItemCount() == 0) {
            setStatus(bookStatusLabel, "No available slots.", false);
            return;
        }

        int participants = (Integer) bookParticipantsSpinner.getValue();

        // Validate participant count
        if (participants > 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Maximum number of participants is 3!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        int slotIdx          = bookSlotCombo.getSelectedIndex() + 1;
        AppointmentType type = (AppointmentType) bookTypeCombo.getSelectedItem();
        int duration         = (Integer) bookDurationSpinner.getValue();

        boolean ok = bookingService.bookAppointment(slotIdx, duration, participants, type);

        if (ok) {
            setStatus(bookStatusLabel, "Appointment booked successfully! Status: CONFIRMED", true);
            notificationService.sendReminders();
            refreshAll();
            JOptionPane.showMessageDialog(
                    this,
                    "Booking Done!\n\nYour appointment has been confirmed.",
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            setStatus(bookStatusLabel, "Booking failed. Check slot availability and participant limit.", false);
            JOptionPane.showMessageDialog(
                    this,
                    "Booking failed.\nPlease check slot availability and participant rules.",
                    "Booking Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
    private JPanel buildModifyPanel() {
        JPanel panel = contentPanel("Modify an Existing Booking");

        JPanel form = buildFormContainer();

        modifyOldSlotCombo        = new JComboBox<>();
        modifyNewSlotCombo        = new JComboBox<>();
        modifyTypeCombo           = new JComboBox<>(AppointmentType.values());
        modifyParticipantsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 3, 1));
        modifyDurationSpinner     = new JSpinner(new SpinnerNumberModel(30, 1, 30, 1));
        modifyStatusLabel         = statusLabel();

        modifyTypeCombo.addActionListener(e -> {
            AppointmentType t = (AppointmentType) modifyTypeCombo.getSelectedItem();
            if (t == null) return;
            int max = com.team.appointment.service.TypeRule.maxForType(t);
            ((SpinnerNumberModel) modifyParticipantsSpinner.getModel()).setMaximum(max);
            int cur = (Integer) modifyParticipantsSpinner.getValue();
            if (cur > max) modifyParticipantsSpinner.setValue(max);
        });

        JButton modifyBtn = primaryButton("  Modify Booking  ");
        modifyBtn.addActionListener(e -> performModify());

        addRow(form, 0, "Current Booked Slot :", modifyOldSlotCombo);
        addRow(form, 1, "New Available Slot :",  modifyNewSlotCombo);
        addRow(form, 2, "Appointment Type :",    modifyTypeCombo);
        addRow(form, 3, "Participants :",        modifyParticipantsSpinner);
        addRow(form, 4, "Duration (min) :",      modifyDurationSpinner);
        addButtonRow(form, 5, modifyBtn, modifyStatusLabel);


        panel.add(new JScrollPane(form, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Executes the booking modification from the form inputs.
     * Shows a confirmation dialog on success or an error dialog on failure.
     */
    private void performModify() {
        if (currentUser == null) {
            setStatus(modifyStatusLabel, "Please log in first.", false);
            return;
        }
        if (modifyOldSlotCombo.getItemCount() == 0) {
            setStatus(modifyStatusLabel, "No booked slots to modify.", false);
            return;
        }
        if (modifyNewSlotCombo.getItemCount() == 0) {
            setStatus(modifyStatusLabel, "No available slots to move to.", false);
            return;
        }

        int participants = (Integer) modifyParticipantsSpinner.getValue();

        // Validate participant count
        if (participants > 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: Maximum number of participants is 3!",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int oldIdx = getAllSlotIndex(modifyOldSlotCombo);
        int newIdx = getAllSlotIndex(modifyNewSlotCombo);

        AppointmentType type = (AppointmentType) modifyTypeCombo.getSelectedItem();
        int duration         = (Integer) modifyDurationSpinner.getValue();

        boolean ok = bookingService.modifyBooking(oldIdx, newIdx, duration, participants, type);

        if (ok) {
            setStatus(modifyStatusLabel, "Booking modified successfully!", true);
            notificationService.sendReminders();
            refreshAll();
            JOptionPane.showMessageDialog(
                    this,
                    "Booking Modified Successfully!",
                    "Modification Confirmed",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            setStatus(modifyStatusLabel, "Modification failed. Check selections.", false);
            JOptionPane.showMessageDialog(
                    this,
                    "Modification failed.\nPlease check your selections and participant rules.",
                    "Modification Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /** Validates inputs and registers a new user account via the auth service. */
  

   
    private JPanel buildCancelPanel() {
        JPanel panel = contentPanel("Cancel a Booking");

        JPanel form = buildFormContainer();

        cancelSlotCombo   = new JComboBox<>();
        cancelStatusLabel = statusLabel();

        JButton cancelBtn = dangerButton("  Cancel Booking  ");
        cancelBtn.addActionListener(e -> performCancel());

        addRow(form, 0, "Select Booked Slot :", cancelSlotCombo);
        addButtonRow(form, 1, cancelBtn, cancelStatusLabel);

        panel.add(new JScrollPane(form, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return panel;
    }

    /** Cancels the selected booked slot and refreshes the UI. */
    private void performCancel() {
        if (currentUser == null) {
            setStatus(cancelStatusLabel, "Please log in first.", false);
            return;
        }
        if (cancelSlotCombo.getItemCount() == 0) {
            setStatus(cancelStatusLabel, "No booked slots available.", false);
            return;
        }
        int allIdx = getAllSlotIndex(cancelSlotCombo);
        boolean ok = bookingService.cancelBooking(allIdx);
        if (ok) {
            setStatus(cancelStatusLabel, "Appointment cancelled. Slot is now available.", true);
            refreshAll();
        } else {
            setStatus(cancelStatusLabel, "Cancellation failed.", false);
        }
    }

    private JPanel buildNotificationsPanel() {
        JPanel panel = contentPanel("Notification Log");

        notificationLogArea = new JTextArea();
        notificationLogArea.setFont(FONT_MONO);
        notificationLogArea.setEditable(false);
        notificationLogArea.setBackground(new Color(15, 23, 42));
        notificationLogArea.setForeground(new Color(134, 239, 172));
        notificationLogArea.setMargin(new Insets(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(notificationLogArea);
        scroll.setBorder(new LineBorder(new Color(30, 58, 138), 2));

        guiNotifier         = new GUINotifier(notificationLogArea);
        EmailNotifier emailNotifier = new EmailNotifier();
        CompositeNotifier composite = new CompositeNotifier(guiNotifier, emailNotifier);
        notificationService = new NotificationService(slotService, composite);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        toolbar.setOpaque(false);

        JButton sendBtn = accentButton("Send Reminders");
        sendBtn.addActionListener(e -> {
            notificationService.sendReminders();
            notificationLogArea.append("──────── Reminders sent ────────\n");
        });

        JButton clearBtn = styledButton("Clear Log", new Color(226, 232, 240), TEXT_DARK);
        clearBtn.addActionListener(e -> notificationLogArea.setText(""));

        toolbar.add(sendBtn);
        toolbar.add(clearBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);
        return panel;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tab 6 – Admin Panel
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Builds the "Administrator Panel" for adding new slots.
     *
     * @return the panel
     */        JPanel addUserSection = sectionPanel("Add New User");
     JPanel userForm = buildFormContainer();

     adminNewUsernameField  = styledTextField("");
     adminNewPasswordField  = new JPasswordField();
     styleTextField(adminNewPasswordField, "");
     adminIsAdminCheckBox   = new JCheckBox("Grant admin privileges");
     adminIsAdminCheckBox.setFont(FONT_BODY);
     adminIsAdminCheckBox.setOpaque(false);
     adminUserStatusLabel   = statusLabel();

     JButton addUserBtn = accentButton("  Add User  ");
     addUserBtn.addActionListener(e -> performAddUser());

     addRow(userForm, 0, "Username :", adminNewUsernameField);
     addRow(userForm, 1, "Password :", adminNewPasswordField);

     GridBagConstraints gc2 = new GridBagConstraints();
     gc2.insets = new Insets(4, 8, 4, 8);
     gc2.gridx = 0; gc2.gridy = 2; gc2.gridwidth = 2;
     gc2.anchor = GridBagConstraints.WEST;
     userForm.add(adminIsAdminCheckBox, gc2);
     addButtonRow(userForm, 3, addUserBtn, adminUserStatusLabel);

     addUserSection.add(userForm, BorderLayout.CENTER);
     wrapper.add(addUserSection, BorderLayout.SOUTH);

    private JPanel buildAdminPanel() {
        JPanel panel = contentPanel("Administrator Panel");

        // ── Add Slot Section ──────────────────────────────────────────────────
        JPanel addSlotSection = sectionPanel("Add New Appointment Slot");
        JPanel slotForm = buildFormContainer();

        adminDateField   = styledTextField("2026-05-01");
        adminTimeField   = styledTextField("09:00");
        adminStatusLabel = statusLabel();

        JLabel noteLabel = new JLabel("Duration is fixed at 30 minutes.");
        noteLabel.setFont(FONT_SMALL);
        noteLabel.setForeground(TEXT_LIGHT);

        JButton addSlotBtn = accentButton("  Add Slot  ");
        addSlotBtn.addActionListener(e -> performAddSlot());

        addRow(slotForm, 0, "Date (YYYY-MM-DD) :", adminDateField);
        addRow(slotForm, 1, "Time (HH:mm) :",      adminTimeField);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 8, 4, 8);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        slotForm.add(noteLabel, gc);
        addButtonRow(slotForm, 3, addSlotBtn, adminStatusLabel);

        addSlotSection.add(slotForm, BorderLayout.CENTER);

        // ── Add User Section ──────────────────────────────────────────────────
        JPanel addUserSection = sectionPanel("Add New User");
        JPanel userForm = buildFormContainer();

        adminNewUsernameField = styledTextField("");
        adminNewPasswordField = new JPasswordField();
        styleTextField(adminNewPasswordField, "");
        adminIsAdminCheckBox  = new JCheckBox("Grant Admin privileges");
        adminIsAdminCheckBox.setFont(FONT_BODY);
        adminIsAdminCheckBox.setOpaque(false);
        adminUserStatusLabel  = statusLabel();

        JButton addUserBtn = primaryButton("  Add User  ");
        addUserBtn.addActionListener(e -> performAddUser1());

        addRow(userForm, 0, "Username :", adminNewUsernameField);
        addRow(userForm, 1, "Password :", adminNewPasswordField);

        GridBagConstraints cb = new GridBagConstraints();
        cb.gridx = 0; cb.gridy = 2; cb.gridwidth = 2;
        cb.insets = new Insets(6, 8, 6, 8);
        cb.anchor = GridBagConstraints.WEST;
        userForm.add(adminIsAdminCheckBox, cb);
        addButtonRow(userForm, 3, addUserBtn, adminUserStatusLabel);

        addUserSection.add(userForm, BorderLayout.CENTER);

        // ── Access Note ───────────────────────────────────────────────────────
        JLabel accessNote = new JLabel(
                "<html><b>Admin-only:</b> Adding slots, adding users, and modifying "
                + "any reservation are restricted to admin accounts.</html>");
        accessNote.setFont(FONT_SMALL);
        accessNote.setForeground(TEXT_LIGHT);
        accessNote.setBorder(new EmptyBorder(6, 0, 0, 0));

        // ── Wrapper ───────────────────────────────────────────────────────────
        JPanel topSection = new JPanel(new GridLayout(1, 2, 16, 0));
        topSection.setOpaque(false);
        topSection.add(addSlotSection);
        topSection.add(addUserSection);

        JPanel wrapper = new JPanel(new BorderLayout(0, 12));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10, 16, 10, 16));
        wrapper.add(topSection,  BorderLayout.CENTER);
        wrapper.add(accessNote,  BorderLayout.SOUTH);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }
    /** Validates inputs and registers a new user account. */
    /** Validates inputs and registers a new user account via the auth service. */
    private void performAddUser() {
        if (currentUser == null || !currentUser.isAdmin()) {
            setStatus(adminUserStatusLabel, "Access denied. Admin privileges required.", false);
            return;
        }
        String username = adminNewUsernameField.getText().trim();
        String password = new String(adminNewPasswordField.getPassword()).trim();
        if (username.isEmpty()) {
            setStatus(adminUserStatusLabel, "Username cannot be empty.", false);
            return;
        }
        if (password.isEmpty()) {
            setStatus(adminUserStatusLabel, "Password cannot be empty.", false);
            return;
        }
        boolean isAdmin = adminIsAdminCheckBox.isSelected();
        boolean ok = authService.addUser(username, password, isAdmin);
        if (ok) {
            setStatus(adminUserStatusLabel, "User '" + username + "' added successfully.", true);
            adminNewUsernameField.setText("");
            adminNewPasswordField.setText("");
            adminIsAdminCheckBox.setSelected(false);
        } else {
            setStatus(adminUserStatusLabel, "Username '" + username + "' already exists.", false);
        }
    }


    private void performAddSlot() {
        if (currentUser == null || !currentUser.isAdmin()) {
            setStatus(adminStatusLabel, "Access denied. Admin privileges required.", false);
            return;
        }
        String date = adminDateField.getText().trim();
        String time = adminTimeField.getText().trim();
        if (date.isEmpty() || time.isEmpty()) {
            setStatus(adminStatusLabel, "Please enter both date and time.", false); return;
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            setStatus(adminStatusLabel, "Date must be in YYYY-MM-DD format.", false); return;
        }
        if (!time.matches("\\d{2}:\\d{2}")) {
            setStatus(adminStatusLabel, "Time must be in HH:mm format.", false); return;
        }
        slotService.addSlot(date, time, 30);
        setStatus(adminStatusLabel, "Slot added: " + date + " at " + time, true);
        refreshAll();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Login / Logout
    // ─────────────────────────────────────────────────────────────────────────

    private void onLoginSuccess() {
        headerUserLabel.setText("Logged in as: " + currentUser.getUsername()
                + (currentUser.isAdmin() ? "  [ADMIN]" : ""));
        tabbedPane.setEnabledAt(5, currentUser.isAdmin());
        tabbedPane.setForegroundAt(5, currentUser.isAdmin() ? PRIMARY : TEXT_LIGHT);
        refreshAll();
        cardLayout.show(rootPanel, "DASHBOARD");
        loginPasswordField.setText("");
    }

    /** Clears the current session and returns to the login screen. */
    private void performLogout() {
        currentUser = null;
        cardLayout.show(rootPanel, "LOGIN");
        loginErrorLabel.setText(" ");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Refresh helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Refreshes all dynamic UI elements (table, combos). */
    private void refreshAll() {
        refreshSlotsTable();
        refreshBookCombo();
        refreshModifyCombos();
        refreshCancelCombo();
    }

    private void refreshBookCombo() {
        bookSlotCombo.removeAllItems();
        for (AppointmentSlot s : slotService.getAvailableSlots())
            bookSlotCombo.addItem(s.getDate() + "   " + s.getTime());
    }

    private void refreshModifyCombos() {
        modifyOldSlotCombo.removeAllItems();
        modifyNewSlotCombo.removeAllItems();
        List<AppointmentSlot> all = slotService.getAllSlots();
        for (int i = 0; i < all.size(); i++) {
            AppointmentSlot s = all.get(i);
            if (s.isBooked())
                modifyOldSlotCombo.addItem((i + 1) + ": " + s.getDate() + " " + s.getTime()
                        + " [" + (s.getType() != null ? s.getType() : "-") + "]");
            else
                modifyNewSlotCombo.addItem((i + 1) + ": " + s.getDate() + " " + s.getTime());
        }
    }

    private void refreshCancelCombo() {
        cancelSlotCombo.removeAllItems();
        List<AppointmentSlot> all = slotService.getAllSlots();
        for (int i = 0; i < all.size(); i++) {
            AppointmentSlot s = all.get(i);
            if (s.isBooked())
                cancelSlotCombo.addItem((i + 1) + ": " + s.getDate() + " " + s.getTime()
                        + "  [" + s.getParticipantCount() + " pax | " + s.getType() + "]");
        }
    }

  
    private int getAllSlotIndex(JComboBox<String> combo) {
        String item = (String) combo.getSelectedItem();
        if (item == null) return 0;
        try { return Integer.parseInt(item.split(":")[0].trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private JPanel buildFormContainer() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(new EmptyBorder(20, 30, 20, 30));
        return form;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addRow(JPanel form, int row, String labelText, JComponent field) {
        GridBagConstraints lc = new GridBagConstraints();
        lc.gridx = 0; lc.gridy = row;
        lc.insets  = new Insets(10, 8, 10, 16);
        lc.anchor  = GridBagConstraints.WEST;
        lc.fill    = GridBagConstraints.NONE;

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_DARK);
        lbl.setPreferredSize(new Dimension(LABEL_WIDTH, FIELD_HEIGHT));
        form.add(lbl, lc);

        GridBagConstraints fc = new GridBagConstraints();
        fc.gridx   = 1; fc.gridy = row;
        fc.insets  = new Insets(10, 0, 10, 8);
        fc.fill    = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;

        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));

        if (field instanceof JComboBox) {
            JComboBox combo = (JComboBox) field;
            combo.setForeground(TEXT_DARK);
            combo.setBackground(Color.WHITE);
            combo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setFont(FONT_BODY);
                    setText(value != null ? value.toString() : "");
                    if (!isSelected) { setForeground(TEXT_DARK); setBackground(Color.WHITE); }
                    return this;
                }
            });
        } else if (field instanceof JTextField) {
            styleTextField((JTextField) field, "");
        } else if (field instanceof JSpinner) {
            field.setBackground(Color.WHITE);
            ((JSpinner.DefaultEditor) ((JSpinner) field).getEditor())
                    .getTextField().setFont(FONT_BODY);
        }

        form.add(field, fc);
    }

    
    private void addButtonRow(JPanel form, int row, JButton btn, JLabel status) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 2;
        gc.insets  = new Insets(16, 8, 6, 8);
        gc.anchor  = GridBagConstraints.WEST;
        form.add(btn, gc);

        gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = row + 1; gc.gridwidth = 2;
        gc.insets  = new Insets(4, 8, 10, 8);
        gc.anchor  = GridBagConstraints.WEST;
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        form.add(status, gc);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI Factory Helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a content panel with a title heading and BorderLayout.
     *
     * @param title the heading text
     * @return the configured panel
     */
    private JPanel contentPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));
        JLabel heading = new JLabel(title);
        heading.setFont(FONT_H2);
        heading.setForeground(PRIMARY);
        heading.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, SECONDARY),
                new EmptyBorder(0, 0, 10, 0)));
        panel.add(heading, BorderLayout.NORTH);
        return panel;
    }

    /**
     * Creates a titled section panel with a border.
     *
     * @param title the section title
     * @return the configured panel
     */
    private JPanel sectionPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SURFACE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(SECONDARY, 1, true), "  " + title + "  ",
                        TitledBorder.LEFT, TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13), SECONDARY),
                new EmptyBorder(8, 12, 12, 12)));
        return p;
    }

    private JButton primaryButton(String text) { return styledButton(text, PRIMARY,  Color.WHITE); }
    private JButton accentButton(String text)  { return styledButton(text, ACCENT,   Color.WHITE); }
    private JButton dangerButton(String text)  { return styledButton(text, DANGER,   Color.WHITE); }

    /**
     * Creates a styled button with hover effect.
     *
     * @param text the button label
     * @param bg   the background color
     * @param fg   the foreground (text) color
     * @return the configured button
     */
    private JButton styledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    /**
     * Creates a bold label with the standard dark text colour.
     *
     * @param text the label text
     * @return the configured label
     */
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    /**
     * Creates a styled text field with the given initial text.
     *
     * @param placeholder initial text content
     * @return the configured text field
     */
    private JTextField styledTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder);
        styleTextField(tf, placeholder);
        return tf;
    }

    /**
     * Applies consistent styling to a text field.
     *
     * @param tf          the text field to style
     * @param placeholder ignored (kept for signature compatibility)
     */
    private void styleTextField(JTextField tf, String placeholder) {
        tf.setFont(FONT_BODY);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                new EmptyBorder(6, 10, 6, 10)));
    }

    /**
     * Creates a blank status label (used below action buttons).
     *
     * @return the status label
     */
    private JLabel statusLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    /**
     * Updates a status label with a success or failure message.
     *
     * @param lbl the label to update
     * @param msg the message text
     * @param ok  {@code true} for success (green tick), {@code false} for error (red cross)
     */
    private void setStatus(JLabel lbl, String msg, boolean ok) {
        lbl.setText(ok ? "\u2705 " + msg : "\u274C " + msg);
        lbl.setForeground(ok ? ACCENT : DANGER);
    }

    /**
     * Applies consistent visual styling to a JTable including alternating row
     * colours and a styled header.
     *
     * @param table the table to style
     */
    private void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(186, 230, 253));
        table.setSelectionForeground(TEXT_DARK);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(TABLE_HEADER);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? SURFACE : new Color(241, 245, 249));
                if (col == 6) {
                    String v = val != null ? val.toString() : "";
                    setForeground("CONFIRMED".equals(v)
                            ? new Color(5, 150, 105) : new Color(217, 119, 6));
                } else {
                    setForeground(TEXT_DARK);
                }
                return this;
            }
        });
    }
}
