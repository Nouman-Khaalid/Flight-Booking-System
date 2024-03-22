import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


class Complaint implements Serializable{
        private String name;
        private String email;
        private String complaintText;
        private LocalDateTime timestamp;

        public Complaint(String name, String email, String complaintText, LocalDateTime timestamp) {
            validateInput(name, email, complaintText);
            this.name = name;
            this.email = email;
            this.complaintText = complaintText;
            this.timestamp = timestamp;
        }

        private void validateInput(String name, String email, String complaintText) {
            if (name==null || email==null || complaintText==null) {
                throw new IllegalArgumentException(
                        "Invalid input for Complaint. Name, email, and complaint text must be filled.");
            }
        }
        public String getComplaintText() {
            return complaintText;
        }
        public String toString() {
        return "Complaint{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", complaintText='" + complaintText + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
    }

public class MainMenuProject extends JFrame {

    private static final String BACKGROUND_IMAGE_PATH = "image1.jpg";
    private JPanel currentPanel;
    private JPanel aboutUsPanel;
    private JPanel buttonPanel;
    private boolean isGuest = true;

    public MainMenuProject() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Airline Reservation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon backgroundImageIcon = new ImageIcon(ClassLoader.getSystemResource(BACKGROUND_IMAGE_PATH));
        Image backgroundImage = backgroundImageIcon.getImage().getScaledInstance(1200, 700, Image.SCALE_DEFAULT);
        ImageIcon scaledBackgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(scaledBackgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout());
        add(backgroundLabel);

        buttonPanel = createButtonPanel();
        currentPanel = buttonPanel;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundLabel.add(buttonPanel, gbc);

        setSize(1200, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        String[] buttonLabels = {
                "Flight Schedule",
                "View Booking History",
                "File Complaint",
                "About Us",
                "Back to Main Page"
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        

        for (String label : buttonLabels) {
            JLabel labelComponent = createStyledLabel(label);
            labelComponent.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    handleLabelClick(label);
                }
                public void mouseEntered(MouseEvent e)
                {
                    labelComponent.setFont(new Font("Arial", Font.BOLD, 16));
                    labelComponent.setForeground(Color.GREEN);

                }
                public void mouseExited(MouseEvent e)
                {
                    labelComponent.setFont(new Font("Arial", Font.PLAIN, 16));
                    labelComponent.setForeground(Color.WHITE);
                }
            });

            

            panel.add(labelComponent, gbc);
            gbc.gridy++;
        }

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        return createStyledLabel(text, new Font("Arial", Font.PLAIN, 16), Color.WHITE);
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private void handleLabelClick(String label) {
        switch (label) {
            case "Flight Schedule":
                new FlightScheduleGui();
                dispose();
                break;
            case "View Booking History":
                showBookingHistory();
                break;
            case "File Complaint":
                showComplaintForm();
                break;
            case "About Us":
                showAboutUsPanel();
                break;
            case "Back to Main Page":
                new MainPage();
                dispose();
                break;
        }
    }



    private void showAboutUsPanel() {
        if (aboutUsPanel == null) {
            aboutUsPanel = createAboutUsPanel();
        }
        JFrame aboutUsFrame = new JFrame("About Us");
        aboutUsFrame.setSize(400, 300);
        aboutUsFrame.add(aboutUsPanel);
        aboutUsFrame.setLocationRelativeTo(null);
        aboutUsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        aboutUsFrame.setVisible(true);

        currentPanel = aboutUsPanel;
    }

    private JPanel createAboutUsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea aboutUsTextArea = new JTextArea(
                "Welcome to JHAN Airlines!\n\n" +
                        "We strive to provide excellent service and ensure your travel experience is comfortable and enjoyable.\n"
                        +
                        "Our commitment is to make your journey with us a memorable one.\n\n" +
                        "OWNER: Muhammad Abdullah\n" +
                        "CEO: Hamza Saif\n" +
                        "GM: Jaun Elia (AR)\n" +
                        "COUNTRY HEAD: Nouman Khan\n" +
                        "\n" +
                        "Contact us:\n" +
                        "Email: info@yourJHANairline.com\n" +
                        "Phone: +92 555-666-7890");
        aboutUsTextArea.setEditable(false);
        aboutUsTextArea.setLineWrap(true);
        aboutUsTextArea.setWrapStyleWord(true);
        aboutUsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        aboutUsTextArea.setForeground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(aboutUsTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void handleFileReadError(IOException ex) {
        JOptionPane.showMessageDialog(null, "Error reading file.");
        ex.printStackTrace();
    }

    private void showBookingHistory() {
        if (isGuest) {
            JOptionPane.showMessageDialog(null, "Sign in as user to see history");
            return;
        }

        JFrame bookingHistoryFrame = new JFrame("Booking History");
        bookingHistoryFrame.setSize(600, 400);

        JPanel bookingHistoryPanel = createBookingHistoryPanel();

        bookingHistoryFrame.add(bookingHistoryPanel);
        bookingHistoryFrame.setLocationRelativeTo(null);
        bookingHistoryFrame.setVisible(true);

        currentPanel = bookingHistoryPanel;
    }

    private JPanel createBookingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JTextArea bookingHistoryTextArea = new JTextArea();
        bookingHistoryTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingHistoryTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        if (!isGuest) {
            List<String> bookingHistory = null;//readBookingHistory();
            if (bookingHistory.isEmpty()) {
                bookingHistoryTextArea.append("No booking history available.");
            } else {
                for (String entry : bookingHistory) {
                    bookingHistoryTextArea.append(entry + "\n");
                }
            }
        } else {
            bookingHistoryTextArea.append("Booking history is not available for guests.");
        }

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void showComplaintForm() {
        JFrame complaintFrame = new JFrame("File a Complaint");
        complaintFrame.setSize(500, 300);

        // Declare the complaintFrame variable here
        JPanel complaintPanel;
        JLabel nameLabel;
        JTextField nameField;
        JLabel emailLabel;
        JTextField emailField;
        JLabel complaintLabel;
        JTextArea complaintArea;
        JScrollPane complaintScrollPane;
        JButton submitButton;

        complaintPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        complaintPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 20));

        emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 20));

        complaintLabel = new JLabel("Complaint:");
        complaintArea = new JTextArea();
        complaintScrollPane = new JScrollPane(complaintArea);
        complaintArea.setPreferredSize(new Dimension(200, 100));

        // Remove vertical scrollbar
        complaintScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        submitButton = createStyledButton("Submit Complaint");

        complaintPanel.add(nameLabel);
        complaintPanel.add(nameField);
        complaintPanel.add(emailLabel);
        complaintPanel.add(emailField);
        complaintPanel.add(complaintLabel);
        complaintPanel.add(complaintScrollPane);

        complaintFrame.add(complaintPanel, BorderLayout.CENTER);
        complaintFrame.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String complaintText = complaintArea.getText().trim();

            if (isValidComplaintInput(name, email, complaintText)) {
                Complaint complaint = new Complaint(name, email, complaintText, LocalDateTime.now());
                saveComplaint(complaint);
                JOptionPane.showMessageDialog(null, "Complaint submitted successfully!");
                complaintFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid input. Please check your entries.");
            }
        });

        submitButton.addMouseListener(new MouseAdapter() {
             public void mouseEntered(MouseEvent e)
                {
                    submitButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
                    submitButton.setForeground(Color.GREEN);

                }
                public void mouseExited(MouseEvent e)
                {
                    submitButton.setFont(new Font("Times New Roman", Font.PLAIN, 18));
                    submitButton.setForeground(Color.BLACK);
                }
        });

        complaintFrame.setLocationRelativeTo(null);
        complaintFrame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        Font buttonFont = new Font("Times New Roman", Font.PLAIN, 18);
        button.setFont(buttonFont);
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private boolean isValidComplaintInput(String name, String email, String complaintText) {
        if (isNullOrEmpty(name) || isNullOrEmpty(email) || isNullOrEmpty(complaintText)) {
            JOptionPane.showMessageDialog(null, "All fields must be filled.");
            return false;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Invalid email address.");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void saveComplaint(Complaint complaint) {
        try (ObjectOutputStream oot = new ObjectOutputStream(
                new FileOutputStream("complaints.bin", true))) {

            oot.writeObject(complaint);
            oot.close();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving complaint.");
            e.printStackTrace();
        }
    }

    

    public static void main(String[] args) {
        new MainMenuProject();
    }
}

