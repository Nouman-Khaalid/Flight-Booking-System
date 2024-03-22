import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;


class FileUtils {
    public static <T extends Serializable> void saveToFile(String filename, ArrayList<T> data) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class AddFlightFrame extends JFrame {
    private JTextField flightNumberField;
    private JTextField departureDateField;
    private JTextField departureAirportField;
    private JTextField destinationAirportField;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;
    private JTextField priceField;
    private JTextField airlineField;
    private JTextField stopsField;

    private ArrayList<FlightSchedule> flights;
    private FlightManagementSystemAdminGUI parent;
    private JComboBox<String> departureDateComboBox;

    public AddFlightFrame(ArrayList<FlightSchedule> flights, FlightManagementSystemAdminGUI parent) {
        this.flights = flights;
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Add FlightSchedule");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = createInputPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFlight();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(9, 2));

        // Sample departure date options, you can replace this with your own logic to generate dates
        String[] departureDates = {"2023-01-01", "2023-01-02", "2023-01-03", "2023-01-04"};

        // Create the dropdown menu for departure date
        departureDateComboBox = new JComboBox<>(departureDates);

        flightNumberField = new JTextField();
        departureAirportField = new JTextField();
        destinationAirportField = new JTextField();
        departureTimeField = new JTextField();
        arrivalTimeField = new JTextField();
        priceField = new JTextField();
        airlineField = new JTextField();
        stopsField = new JTextField();

        inputPanel.add(new JLabel("FlightSchedule Number:"));
        inputPanel.add(flightNumberField);
        inputPanel.add(new JLabel("Departure Date:"));
        inputPanel.add(departureDateComboBox);
        inputPanel.add(new JLabel("Departure Airport:"));
        inputPanel.add(departureAirportField);
        inputPanel.add(new JLabel("Destination Airport:"));
        inputPanel.add(destinationAirportField);
        inputPanel.add(new JLabel("Departure Time:"));
        inputPanel.add(departureTimeField);
        inputPanel.add(new JLabel("Arrival Time:"));
        inputPanel.add(arrivalTimeField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Airline:"));
        inputPanel.add(airlineField);
        inputPanel.add(new JLabel("Stops:"));
        inputPanel.add(stopsField);

        return inputPanel;
    }

    public void setFlightDetails(FlightSchedule flight) {
        flightNumberField.setText(flight.getFlightId());
        departureDateComboBox.setSelectedItem(flight.getDepartureDate());
        departureAirportField.setText(flight.getDeparture());
        destinationAirportField.setText(flight.getDestination());
        departureTimeField.setText(flight.getDepartureTime());
        arrivalTimeField.setText(flight.getArrivalTime());
        priceField.setText(String.valueOf(flight.getTicketPrice()));
        airlineField.setText(flight.getAirline());
        stopsField.setText(String.valueOf(flight.getStops()));
    }

    private void addFlight() {
        String flightId = flightNumberField.getText();
        String departure = departureAirportField.getText();
        String destination = destinationAirportField.getText();
        String departureTime = departureTimeField.getText();
        String arrivalTime = arrivalTimeField.getText();
        String priceText = priceField.getText();
        String airline = airlineField.getText();
        String stopsText = stopsField.getText();
        String selectedDepartureDate = (String) departureDateComboBox.getSelectedItem();

        // Validate input fields
        if (isNullOrEmpty(flightId) || isNullOrEmpty(departure) || isNullOrEmpty(destination)
                || isNullOrEmpty(departureTime) || isNullOrEmpty(arrivalTime) || isNullOrEmpty(priceText) || isNullOrEmpty(airline) || isNullOrEmpty(stopsText)) {
            JOptionPane.showMessageDialog(this, "All fields must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate price as a double
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price format. Please enter a valid non-negative number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate stops as an integer
        int stops;
        try {
            stops = Integer.parseInt(stopsText);
            if (stops < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid stops format. Please enter a valid non-negative integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FlightSchedule flight = new FlightSchedule(departure,destination,airline, selectedDepartureDate, departureTime,
        arrivalTime, price,  stops, flightId);

        flights.add(flight);
        saveToFile("flightschedule.dat", flights);
        JOptionPane.showMessageDialog(this, "FlightSchedule added successfully!");
        clearFields();
        parent.updateFlightList();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private void clearFields() {
        flightNumberField.setText("");
        //departureDateField.setText("");
        departureAirportField.setText("");
        destinationAirportField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        priceField.setText("");
        airlineField.setText("");
        stopsField.setText("");
    }

    private <T extends Serializable> void saveToFile(String filename, ArrayList<T> data) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data to file: " + e.getMessage());
        }
    }
}

// DeleteFlightFrame
class DeleteFlightFrame extends JFrame {
    private JTextField flightNumberField;
    private ArrayList<FlightSchedule> flights;
    private FlightManagementSystemAdminGUI parent;

    public DeleteFlightFrame(ArrayList<FlightSchedule> flights, FlightManagementSystemAdminGUI parent) {
	    this.flights = flights;
	    this.parent = parent;
	    initializeUI();
	}

    private void initializeUI() {
        setTitle("Delete FlightSchedule");
        setSize(300, 110);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));

        flightNumberField = new JTextField();
        inputPanel.add(new JLabel("FlightSchedule Number:"));
        inputPanel.add(flightNumberField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFlight();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

     private void deleteFlight() {
        String flightId = flightNumberField.getText();
        if (isNullOrEmpty(flightId)) {
            JOptionPane.showMessageDialog(this, "FlightSchedule Number must be provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (FlightSchedule flight : flights) {
            if (flight.getFlightId().equals(flightId)) {
                flights.remove(flight);
                FileUtils.saveToFile("flightschedule.dat", flights);
                JOptionPane.showMessageDialog(this, "FlightSchedule deleted successfully!");
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "FlightSchedule not found.");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}

// EditFlightFrame
class EditFlightFrame extends JFrame {
    private JTextField flightNumberField;
    private ArrayList<FlightSchedule> flights;
    private FlightManagementSystemAdminGUI parent;

    public EditFlightFrame(ArrayList<FlightSchedule> flights, FlightManagementSystemAdminGUI parent) {
        this.flights = flights;
        this.parent = parent;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Edit FlightSchedule");
        setSize(300, 110);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));

        flightNumberField = new JTextField(8);
        inputPanel.add(new JLabel("FlightSchedule Number:"));
        inputPanel.add(flightNumberField);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFlight();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void editFlight() {
        String flightId = flightNumberField.getText();
        if (isNullOrEmpty(flightId)) {
            JOptionPane.showMessageDialog(this, "FlightSchedule Number must be provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (FlightSchedule flight : flights) {
            if (flight.getFlightId().equals(flightId)) {
                // Open the AddFlightFrame with the existing flight details
                AddFlightFrame addFlightFrame = new AddFlightFrame(flights, parent);
                addFlightFrame.setFlightDetails(flight);
                addFlightFrame.setVisible(true);
                flights.remove(flight);

                // Close the EditFlightFrame
                dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "FlightSchedule not found.");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
// SeeComplaintsFrame
class SeeComplaintsFrame extends JFrame {
    private ArrayList<Complaint> complaints;

    public SeeComplaintsFrame(ArrayList<Complaint> complaints) {
        this.complaints = complaints;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("See Complaints");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        for (Complaint complaint : complaints) {
            displayArea.append(complaint.toString() + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(displayArea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}

// SeeBookingsFrame
class SeeBookingsFrame extends JFrame {
    private ArrayList<Booking> bookings;

    public SeeBookingsFrame(ArrayList<Booking> bookings) {
        this.bookings = bookings;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("See Bookings");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        for (Booking booking : bookings) {
            displayArea.append(booking.toString() + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(displayArea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}

// SearchBookingFrame
class SearchBookingFrame extends JFrame {
    private JTextField bookingNumberField;
    private ArrayList<Booking> bookings;

    public SearchBookingFrame(ArrayList<Booking> bookings) {
        this.bookings = bookings;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Search Booking");
        setSize(300, 110);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(1, 2));

        bookingNumberField = new JTextField();
        inputPanel.add(new JLabel("Booking Number:"));
        inputPanel.add(bookingNumberField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooking();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void searchBooking() {
        String bookingNumber = bookingNumberField.getText();
        if (isNullOrEmpty(bookingNumber)) {
            JOptionPane.showMessageDialog(this, "Booking Number must be provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Booking booking : bookings) {
            if (booking.getBookingNumber().equals(bookingNumber)) {
                JTextArea displayArea = new JTextArea();
                displayArea.setEditable(false);
                displayArea.append("Booking Details:\n");
                displayArea.append(booking.toString());
                JScrollPane scrollPane = new JScrollPane(displayArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Booking Details", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Booking not found.");
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}

// SeeFlightsFrame
class SeeFlightsFrame extends JFrame {
    private ArrayList<FlightSchedule> flights;

    public SeeFlightsFrame(ArrayList<FlightSchedule> flights) {
        this.flights = flights;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("See Flights");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        for (FlightSchedule flight : flights) {
            displayArea.append(flight.toString() + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(displayArea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}

class SeeUsersFrame extends JFrame {
    private ArrayList<User> users;

    public SeeUsersFrame(ArrayList<User> users) {
        this.users = users;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("See Users");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);

        for (User user : users) {
            displayArea.append(user.toString() + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(displayArea);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }
}

class FlightManagementSystemAdminGUI extends JFrame {
    private ArrayList<FlightSchedule> flights = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private ArrayList<Complaint> complaints = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();

    private DefaultListModel<String> flightListModel;
    private JList<String> flightList;

    public FlightManagementSystemAdminGUI() {
        flights = loadFromFileFlights("flightschedule.dat");       
        complaints = loadFromFileComplaints("complaints.bin");
        bookings = loadFromFileBookings("bookings.dat");
        users = loadFromFileUsers("users.bin");

        initializeUI();
    }

    private void initializeUI() {
        setTitle("FlightSchedule Management System Admin");
        setSize(1220,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flightListModel = new DefaultListModel<>();
        flightList = new JList<>(flightListModel);
        JScrollPane scrollPane = new JScrollPane(flightList);

        JPanel buttonPanel = createButtonPanel();

        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());

        // Set the background image
        try {
            InputStream backgroundImageStream = getClass().getResourceAsStream("planeflying.jpg");
            if (backgroundImageStream != null) {
                ImageIcon backgroundImage = new ImageIcon(ImageIO.read(backgroundImageStream));
                backgroundLabel.setIcon(backgroundImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        backgroundLabel.add(buttonPanel, BorderLayout.NORTH);

        // Add the backgroundLabel to the main container
        setLayout(new BorderLayout());
        add(backgroundLabel);

        //setLayout(new GridLayout());
        //add(scrollPane);
        //add(buttonPanel);

        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 15, 15);

        JButton addFlightButton = new JButton("Add FlightSchedule");
        addButtonToGrid(buttonPanel, addFlightButton, gbc, 3, 3);

        JButton deleteFlightButton = new JButton("Delete FlightSchedule");
        addButtonToGrid(buttonPanel, deleteFlightButton, gbc, 3, 4);

        JButton editFlightButton = new JButton("Edit FlightSchedule");
        addButtonToGrid(buttonPanel, editFlightButton, gbc, 3, 5);

        JButton seeComplaintsButton = new JButton("See Complaints");
        addButtonToGrid(buttonPanel, seeComplaintsButton, gbc, 3, 6);

        JButton seeBookingsButton = new JButton("See Bookings");
        addButtonToGrid(buttonPanel, seeBookingsButton, gbc, 4, 3);

        JButton searchBookingButton = new JButton("Search Booking");
        addButtonToGrid(buttonPanel, searchBookingButton, gbc, 4, 4);

        JButton seeFlightsButton = new JButton("See Flights");
        addButtonToGrid(buttonPanel, seeFlightsButton, gbc, 4, 5);

        JButton seeUsersButton = new JButton("See Users");
        addButtonToGrid(buttonPanel, seeUsersButton, gbc, 4, 6);

        addFlightButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                addFlightButton.setFont(new Font("Arial", Font.BOLD, 18));
                addFlightButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                addFlightButton.setFont(new Font("Arial", Font.PLAIN, 16));
                addFlightButton.setForeground(Color.BLACK);
            }
        });

        deleteFlightButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                deleteFlightButton.setFont(new Font("Arial", Font.BOLD, 18));
                deleteFlightButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                deleteFlightButton.setFont(new Font("Arial", Font.PLAIN, 16));
                deleteFlightButton.setForeground(Color.BLACK);
            }
        });

        editFlightButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                editFlightButton.setFont(new Font("Arial", Font.BOLD, 18));
                editFlightButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                editFlightButton.setFont(new Font("Arial", Font.PLAIN, 16));
                editFlightButton.setForeground(Color.BLACK);
            }
        });

        seeComplaintsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                seeComplaintsButton.setFont(new Font("Arial", Font.BOLD, 18));
                seeComplaintsButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                seeComplaintsButton.setFont(new Font("Arial", Font.PLAIN, 16));
                seeComplaintsButton.setForeground(Color.BLACK);
            }
        });

        seeBookingsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                seeBookingsButton.setFont(new Font("Arial", Font.BOLD, 18));
                seeBookingsButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                seeBookingsButton.setFont(new Font("Arial", Font.PLAIN, 16));
                seeBookingsButton.setForeground(Color.BLACK);
            }
        });

        searchBookingButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                searchBookingButton.setFont(new Font("Arial", Font.BOLD, 18));
                searchBookingButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                searchBookingButton.setFont(new Font("Arial", Font.PLAIN, 16));
                searchBookingButton.setForeground(Color.BLACK);
            }
            
        });

        seeFlightsButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                seeFlightsButton.setFont(new Font("Arial", Font.BOLD, 18));
                seeFlightsButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                seeFlightsButton.setFont(new Font("Arial", Font.PLAIN, 16));
                seeFlightsButton.setForeground(Color.BLACK);
            }
        });

        seeUsersButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)
            {
                seeUsersButton.setFont(new Font("Arial", Font.BOLD, 18));
                seeUsersButton.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                seeUsersButton.setFont(new Font("Arial", Font.PLAIN, 16));
                seeUsersButton.setForeground(Color.BLACK);
            }
        });

        Dimension buttonSize = new Dimension(180, 70);
        addFlightButton.setPreferredSize(buttonSize);
        deleteFlightButton.setPreferredSize(buttonSize);
        editFlightButton.setPreferredSize(buttonSize);
        seeComplaintsButton.setPreferredSize(buttonSize);
        seeBookingsButton.setPreferredSize(buttonSize);
        searchBookingButton.setPreferredSize(buttonSize);
        seeFlightsButton.setPreferredSize(buttonSize);
        seeUsersButton.setPreferredSize(buttonSize);
        buttonPanel.setOpaque(false);
        return buttonPanel;
    }

    private void addButtonToGrid(JPanel panel, JButton button, GridBagConstraints gbc, int row, int column) {
        gbc.gridx = column;
        gbc.gridy = row;
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.BLACK.darker());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        panel.add(button, gbc);

        button.addActionListener(e -> handleButtonClick(button.getText()));
    }

    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "Add FlightSchedule":
                addFlight();
                break;
            case "Delete FlightSchedule":
                deleteFlight();
                break;
            case "Edit FlightSchedule":
                editFlight();
                break;
            case "See Complaints":
                seeComplaints();
                break;
            case "See Bookings":
                seeBookings();
                break;
            case "Search Booking":
                searchBooking();
                break;
            case "See Flights":
                seeFlights();
                break;
            case "See Users":
                seeUsers();
                break;
        }
    }

    private void addFlight() {
        new AddFlightFrame(flights, this).setVisible(true);
    }

    private void deleteFlight() {
        new DeleteFlightFrame(flights, this).setVisible(true);
    }

    private void editFlight() {
        new EditFlightFrame(flights, this).setVisible(true);
    }

    private void seeComplaints() {
        new SeeComplaintsFrame(complaints).setVisible(true);
    }

    private void seeBookings() {
        new SeeBookingsFrame(bookings).setVisible(true);
    }

    private void searchBooking() {
        new SearchBookingFrame(bookings).setVisible(true);
    }

    private void seeFlights() {
        new SeeFlightsFrame(flights).setVisible(true);
    }

    private void seeUsers() {
        new SeeUsersFrame(users).setVisible(true);
    }

    void updateFlightList() {
        flightListModel.clear();
        for (FlightSchedule flight : flights) {
            flightListModel.addElement(flight.toString());
        }
    }

    private ArrayList<FlightSchedule> loadFromFileFlights(String filename) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            // Correctly cast the read object to FlightSchedule
            return (ArrayList<FlightSchedule>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private  ArrayList<Complaint> loadFromFileComplaints(String filename) {
        ArrayList<Complaint> list= new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            
            while (true) {
                try {
                    Complaint c= (Complaint) inputStream.readObject();
                    list.add(c);
                } catch (EOFException|StreamCorruptedException e) {
                    // Break when end of file is reached
                    break;
                }
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;

    }
    private  ArrayList<Booking> loadFromFileBookings(String filename) {
        ArrayList<Booking> list= new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            
            while (true) {
                try {
                    Booking f = (Booking) inputStream.readObject();
                    list.add(f);
                } catch (EOFException|StreamCorruptedException e) {
                    // Break when end of file is reached
                    break;
                }
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;

    }
    private  ArrayList<User> loadFromFileUsers(String filename) {
        ArrayList<User> list= new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            
            while (true) {
                try {
                    User f = (User) inputStream.readObject();
                    list.add(f);
                } catch (EOFException|StreamCorruptedException e) {
                    // Break when end of file is reached
                    break;
                }
                
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;

    }
        
    
    private <T extends Serializable> void saveToFile(String filename, ArrayList<T> data) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving data to file: " + e.getMessage());
        }
    }
}

public class AdminMenuProject {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlightManagementSystemAdminGUI());
    }
}
