
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

 class RegisterPage {
    private JFrame frame;
    private JTextField firstNameField, lastNameField, usernameField, emailField,
            cnicPassportField, phoneNumberField;
    private JPasswordField passwordField;
    private JComboBox<String> genderComboBox;
    private JCheckBox successCheckBox;
    private JComboBox<String> countryField;

    public RegisterPage() {
        initializeFrame();
        createComponents();
        addComponentsToFrame();
        setFrameVisible(true);
    }

    // Constructor that takes another RegisterPage object as an argument
    public RegisterPage(RegisterPage existingRegisterPage) {
        initializeFrame();
        createComponents();
        addComponentsToFrame();
        setFrameVisible(true);

        copyDataFrom(existingRegisterPage);
    }

    private void copyDataFrom(RegisterPage existingRegisterPage) {
        this.firstNameField.setText(existingRegisterPage.firstNameField.getText());
        this.lastNameField.setText(existingRegisterPage.lastNameField.getText());
        this.usernameField.setText(existingRegisterPage.usernameField.getText());
        this.passwordField.setText(new String(existingRegisterPage.passwordField.getPassword()));
        this.emailField.setText(existingRegisterPage.emailField.getText());
        this.countryField.setSelectedItem(existingRegisterPage.countryField.getSelectedItem());
        this.successCheckBox.setSelected(existingRegisterPage.successCheckBox.isSelected());
        this.genderComboBox.setSelectedItem(existingRegisterPage.genderComboBox.getSelectedItem());
        this.cnicPassportField.setText(existingRegisterPage.cnicPassportField.getText());
        this.phoneNumberField.setText(existingRegisterPage.phoneNumberField.getText());
    }

    private void initializeFrame() {
        frame = new JFrame("Register");
        frame.setSize(600, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void createComponents() {
        firstNameField = new JTextField();
        lastNameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();

        String[] countries = { "Pakistan", "United States", "United Kingdom", "Canada", "Australia", "India", "Germany",
                "France", "China", "Japan" };
        countryField = new JComboBox<>(countries);

        successCheckBox = new JCheckBox("Registration Successful");

        genderComboBox = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        cnicPassportField = new JTextField();
        phoneNumberField = new JTextField();
    }

    private void addComponentsToFrame() {
        JPanel formPanel = new JPanel(new GridLayout(10, 2));
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Country:"));
        formPanel.add(countryField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderComboBox);
        formPanel.add(new JLabel("CNIC/Passport Number:"));
        formPanel.add(cnicPassportField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneNumberField);
        formPanel.add(successCheckBox);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegistration());

        JButton backButton = new JButton("Back to Main Page");
        backButton.addActionListener(e -> showMainPage());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        frame.add(new JLabel("Registration Page"), BorderLayout.NORTH);
        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setFrameVisible(boolean visible) {
        frame.setVisible(visible);
    }

    private void handleRegistration() {
        try {
            if (!successCheckBox.isSelected()) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Please check the 'Registration Successful' checkbox."));
                return;
            }

            if (validateFieldsNotEmpty() && validateRegistration()) {
                processRegistration();
                frame.dispose();
                SwingUtilities.invokeLater(this::showMainPage);
            } else {
                SwingUtilities.invokeLater(
                        () -> JOptionPane.showMessageDialog(frame, "Please fill in all the fields."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateFieldsNotEmpty() {
        if (firstNameField.getText().isEmpty()
                || lastNameField.getText().isEmpty()
                || usernameField.getText().isEmpty()
                || new String(passwordField.getPassword()).isEmpty()
                || emailField.getText().isEmpty()
                || countryField.getSelectedItem() == null
                || genderComboBox.getSelectedItem() == null
                || cnicPassportField.getText().isEmpty()
                || phoneNumberField.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    private void processRegistration() throws FileNotFoundException, IOException {
        String fileName = "users.bin";

        

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            User newUser = new User(
                firstNameField.getText(),
                lastNameField.getText(),
                usernameField.getText(),
                new String(passwordField.getPassword()),
                emailField.getText(),
                (String) countryField.getSelectedItem(),
                (String) genderComboBox.getSelectedItem(),
                cnicPassportField.getText(),
                phoneNumberField.getText());


            oos.writeObject(newUser);
            UserDatabase.registerUser(newUser.getUsername());
            System.out.println("Registration Successful!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error during registration. Please try again.");
        }
    }
    

    

    private User createUserFromFields() {
        return new User(
                firstNameField.getText(),
                lastNameField.getText(),
                usernameField.getText(),
                new String(passwordField.getPassword()),
                emailField.getText(),
                (String) countryField.getSelectedItem(),
                (String) genderComboBox.getSelectedItem(),
                cnicPassportField.getText(),
                phoneNumberField.getText());
    }

    private boolean validateRegistration() {
        String username = usernameField.getText();
        if (UserDatabase.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(frame, "Username is already taken. Please choose a different username.");
            return false;
        }

        String email = emailField.getText();

        return true;
    }

    private void showMainPage() {
        
        new MainPage();
        frame.dispose();
    }

    public static class UserDatabase {
        private static List<String> registeredUsernames = new ArrayList<>();

        public static boolean isUsernameTaken(String username) {
            return registeredUsernames.contains(username);
        }

        public static void registerUser(String username) {
            registeredUsernames.add(username);
        }
    }

    
}

class User implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String country;
    private String gender;
    private String cnicPassport;
    private String phoneNumber;

    public User(String firstName, String lastName, String username, String password, String email,
            String country, String gender, String cnicPassport, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.country = country;
        this.gender = gender;
        this.cnicPassport = cnicPassport;
        this.phoneNumber = phoneNumber;

        if (isNullOrEmpty(firstName) || isNullOrEmpty(lastName) || isNullOrEmpty(username) || isNullOrEmpty(password)
                || isNullOrEmpty(email) || isNullOrEmpty(country) || isNullOrEmpty(gender) || isNullOrEmpty(cnicPassport)
                || isNullOrEmpty(phoneNumber)) {
            throw new IllegalArgumentException("Invalid input for User. All fields must be filled.");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCnicPassport() {
        return cnicPassport;
    }

    public void setCnicPassport(String cnicPassport) {
        this.cnicPassport = cnicPassport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", gender='" + gender + '\'' +
                ", cnicPassport='" + cnicPassport + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    
}

public class MainPage {

    private JFrame frame;

    public MainPage() {
        frame = new JFrame("Airline Reservation System");
        frame.setSize(1360, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

       
        ImageIcon backgroundImage = new ImageIcon("image2.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        frame.setContentPane(backgroundLabel);

        JPanel blackBoxPanel = new JPanel(new GridBagLayout());
        //blackBoxPanel.setBackground(Color.lightGray);
        blackBoxPanel.setOpaque(false);
        blackBoxPanel.setBackground(new Color(0,0,0,0));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(new Color(0,0,0,0));

        JButton registerButton = createStyledButton("Register");
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setBorderPainted(false);
        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                registerButton.setFont(new Font("Arial", Font.BOLD, 18));
                registerButton.setForeground(Color.RED);
            }
            public void mouseExited(MouseEvent e)
            {
                registerButton.setFont(new Font("Arial", Font.BOLD, 16));
                registerButton.setForeground(Color.WHITE);
            }
        });

        JButton loginButton = createStyledButton("Login");
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                loginButton.setFont(new Font("Arial", Font.BOLD, 18));
                loginButton.setForeground(Color.RED);
            }
            public void mouseExited(MouseEvent e)
            {
                loginButton.setFont(new Font("Arial", Font.BOLD, 16));
                loginButton.setForeground(Color.WHITE);
            }
        });

        JButton guestButton = createStyledButton("Continue as Guest");
        guestButton.setOpaque(false);
        guestButton.setContentAreaFilled(false);
        guestButton.setBorderPainted(false);
        guestButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                guestButton.setFont(new Font("Arial", Font.BOLD, 18));
                guestButton.setForeground(Color.RED);
            }
            public void mouseExited(MouseEvent e)
            {
                guestButton.setFont(new Font("Arial", Font.BOLD, 16));
                guestButton.setForeground(Color.WHITE);
            }
        });
        guestButton.addActionListener(e -> {
            new MainMenuProject();
            frame.dispose();
        });

        JButton adminLoginButton = createStyledButton("Admin Login");
        adminLoginButton.setOpaque(false);
        adminLoginButton.setContentAreaFilled(false);
        adminLoginButton.setBorderPainted(false);
        adminLoginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                adminLoginButton.setFont(new Font("Arial", Font.BOLD, 18));
                adminLoginButton.setForeground(Color.RED);
            }
            public void mouseExited(MouseEvent e)
            {
                adminLoginButton.setFont(new Font("Arial", Font.BOLD, 16));
                adminLoginButton.setForeground(Color.WHITE);
            }
        });


        Dimension buttonSize = new Dimension(200, 50);
        registerButton.setPreferredSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);
        guestButton.setPreferredSize(buttonSize);
        adminLoginButton.setPreferredSize(buttonSize);


        registerButton.addActionListener(e -> openRegisterPage());
        loginButton.addActionListener(e -> openLoginPage());
        guestButton.addActionListener(e -> {new MainMenuProject(); frame.dispose();});
        adminLoginButton.addActionListener(e -> openAdminLoginPage());

        // Add buttons to the button panel with constraints for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        buttonPanel.add(registerButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(loginButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(guestButton, gbc);

        gbc.gridy = 3;
        buttonPanel.add(adminLoginButton, gbc);

        // Add the button panel to the black box panel
        blackBoxPanel.add(buttonPanel);

        // Create constraints to center the black box panel
        GridBagConstraints centerGBC = new GridBagConstraints();
        centerGBC.gridx = 0;
        centerGBC.gridy = 0;
        centerGBC.weightx = 1;
        centerGBC.weighty = 1;
        centerGBC.anchor = GridBagConstraints.CENTER;

        // Add the black box panel to the background label at the center
        backgroundLabel.setLayout(new GridBagLayout());
        backgroundLabel.add(blackBoxPanel, centerGBC);

        frame.setVisible(true);

    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(true); // Make the button transparent
        button.setBorderPainted(true); // Remove the border
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE); // Set text color


        return button;
    }

    private void openRegisterPage() {
        new RegisterPage();
        frame.dispose();
    }

    private void openLoginPage() {
        new LoginPage();
        frame.dispose();
    }


    private void openAdminLoginPage() {
        new AdminLoginPage();
        frame.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainPage::new);
    }
}


class AdminLoginPage extends JFrame {

    private static final String ADMIN_NAME = "jhan";
    private static final String PASSWORD = "208";

    public AdminLoginPage() {
        setTitle("Admin Login Page");
        setSize(350, 200);
        setLayout(new BorderLayout());

        //JLabel adminLabel = new JLabel("Admin Login Page");

        JTextField nameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back to Main Page");

        loginButton.addActionListener(e -> {

            String enteredName = nameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            if (enteredName.equals(ADMIN_NAME) && enteredPassword.equals(PASSWORD)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new FlightManagementSystemAdminGUI();
                dispose(); 
            } else {
                if (!enteredName.equals(ADMIN_NAME)) {
                    JOptionPane.showMessageDialog(this, "Wrong name! Please try again.");
                } else {
                    JOptionPane.showMessageDialog(this, "Wrong password! Please try again.");
                }
            }
        });

        backButton.addActionListener(e -> {
            dispose(); 
            openMainPage();
        });

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Name:"));
        loginPanel.add(nameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        //add(adminLabel, BorderLayout.NORTH);
        setLocationRelativeTo(null);
        add(loginPanel, BorderLayout.CENTER);
        setVisible(true);

    }

    private void openMainPage() {
        // Create a new main page instance
        new MainPage();
    }

    public static void main(String[] args) {
        new AdminLoginPage();
    }
}


class GuestPage extends JFrame {

    public GuestPage() {
        setSize(400, 300);
        setLayout(new BorderLayout());

        JLabel guestLabel = new JLabel("Guest Page");
        JButton backButton = new JButton("Back to Main Page");

        backButton.addActionListener(e -> {
            // Open the main page again
            dispose(); // Close the current login window
            openMainPage();
        });

        add(guestLabel, BorderLayout.NORTH);
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void openMainPage() {
        dispose();
        new MainPage(); // Show the MainPage instance
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GuestPage());
    }
}


class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setSize(400, 300);
        setLayout(new BorderLayout());

        JLabel loginLabel = new JLabel("Login Page");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back to Main Page");

        loginButton.addActionListener(e -> handleLogin());

        backButton.addActionListener(e -> {
            // Open the main page again
            dispose(); // Close the current login window
            openMainPage();
        });

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        add(loginLabel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void handleLogin() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = new String(passwordField.getPassword());

        if (UserDatabase.validateLogin(enteredUsername, enteredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            new MainMenuProject();
            dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect username or password. Please try again.");
        }
    }

    private void openMainPage() {
        new MainPage();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }
}

class UserDatabase {
    private static List<User> users;

    

    private static List<User> loadUsersFromFile() {
        List<User> userList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.bin"))) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    userList.add(user);
                } catch (EOFException|StreamCorruptedException e) {
                    // Break when end of file is reached
                    break;
                }
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(userList.size());
        return userList;
    }


    

    public static boolean validateLogin(String enteredUsername, String enteredPassword) {
        users=loadUsersFromFile();
        for (User user : users) {
            if (user.getUsername().equals(enteredUsername) && user.getPassword().equals(enteredPassword)) {
                return true; // Match found
            }
        }
        return false; // No match found
    }
}