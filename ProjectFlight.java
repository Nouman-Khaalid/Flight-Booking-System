import javax.swing.*;
import java.awt.*;
//import java.awt.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



class FlightSchedule implements Serializable
{
    private String departure, destination, airline;
    private String departureDate ,departureTime, arrivalTime, flightId;
    private double ticketPrice;
    private int stops;

    public FlightSchedule(){};
    public FlightSchedule(String departure, String destination, String airline,String departureDate, String departureTime , String arrivalTime, double ticketPrice, int stops, String flightId)
    {
        this.departure=departure;
        this.destination=destination;
        this.airline=airline;
        this.departureDate=departureDate;
        this.departureTime=departureTime;
        this.arrivalTime=arrivalTime;
        this.flightId=flightId;
        if(checkTicketPrice())
            this.ticketPrice = ticketPrice;
        else
            System.out.println("Invalid");
        if(checkStops())
            this.stops = stops;
        else
            System.out.println("Invalid");
    }
    public void setAirline(String airline) {
        this.airline = airline;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public void setDeparture(String departure) {
        this.departure = departure;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public void setTicketPrice(double ticketPrice) {
        if(checkTicketPrice())
            this.ticketPrice = ticketPrice;
        else
            System.out.println("Invalid");
    }
    public String getAirline() {
        return airline;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public String getDeparture() {
        return departure;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public String getDestination() {
        return destination;
    }
    public int getStops() {
        return stops;
    }
    public double getTicketPrice() {
        return ticketPrice;
    }
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }public String getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
    public String getFlightId() {
        return flightId;
    }
    private boolean checkTicketPrice()
    {
        if(ticketPrice<0)
            return false;
        return true;
    }
    private boolean checkStops()
    {
        if(stops<0)
            return false;
        return true;
    }public void setStops(int stops) {
        if (stops < 0) {
            throw new IllegalArgumentException("Stops must be non-negative.");
        }
        this.stops = stops;
    }

    public void writeSchedule(ObjectOutputStream ous) throws IOException {
        ous.writeObject(this);
    }

    public static FlightSchedule readSchedule(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (FlightSchedule) ois.readObject();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightId + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", departureAirport='" + departure + '\'' +
                ", destinationAirport='" + destination + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", price=" + ticketPrice +
                ", airline='" + airline + '\'' +
                ", stops=" + stops +
                '}';
    }

}

class FlightScheduleGui extends JFrame 
{
    private ArrayList<FlightSchedule> schedules;
    

    public FlightScheduleGui() {
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Flight Schedules");
        schedules = loadFromFile();
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel mainPanel= new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel titleJPanel= new JPanel();
        titleJPanel.setLayout(new BorderLayout());
        titleJPanel.setBackground(Color.BLUE);
        JLabel l= new JLabel("FLIGHT SCHEDULES");
        l.setFont(new Font("Arial", Font.BOLD, 28));
        l.setForeground(Color.WHITE);
        titleJPanel.add(l, BorderLayout.WEST);
        JButton back= new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e)
            {
                back.setBackground(Color.WHITE);
                back.setForeground(Color.BLACK);
            }
            public void mouseExited(MouseEvent e)
            {
                back.setBackground(Color.BLACK);
                back.setForeground(Color.WHITE);
            }
        });
        back.addActionListener(e->{
            new MainMenuProject();
            dispose();
        });
        titleJPanel.add(back, BorderLayout.EAST);
        mainPanel.add(titleJPanel, BorderLayout.NORTH);

        


        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        display(mainPanel);
        setContentPane(scrollPane);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private ArrayList<FlightSchedule> loadFromFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("flightschedule.dat"))) {
            // Correctly cast the read object to FlightSchedule
            return (ArrayList<FlightSchedule>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void display(JPanel mainPanel) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        for (FlightSchedule f : schedules) 
        {
            JPanel p = new JPanel();
            p.setPreferredSize(new Dimension(1000, 100));
            p.setLayout(new BorderLayout());
            p.setBackground(Color.BLACK);
            p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            
            JPanel leftP = new JPanel();
            leftP.setBackground(Color.CYAN);
            leftP.setPreferredSize(new Dimension(200, 100));
            leftP.setLayout(new BorderLayout());
            JLabel l = new JLabel(f.getAirline());
            l.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD, 22));
            leftP.add(l);
            p.add(leftP, BorderLayout.WEST);

            JPanel innerP = new JPanel();
            innerP.setLayout(new BorderLayout());
            innerP.setPreferredSize(new Dimension(600, 100));
            l = new JLabel(f.getDepartureDate()+"    "+f.getDepartureTime() + "-" + f.getArrivalTime());
            l.setFont(new Font("Arial", Font.BOLD, 24));
            innerP.add(l, BorderLayout.NORTH);
            l = new JLabel(f.getDeparture() + "- " + f.getStops() + " Stops -" + f.getDestination());
            l.setFont(new Font("Arial", Font.PLAIN, 18));
            innerP.add(l, BorderLayout.AFTER_LAST_LINE);
            p.add(innerP, BorderLayout.CENTER);

            JPanel rightP = new JPanel();
            rightP.setLayout(new GridLayout(2, 1));

            l = new JLabel("PKR " + f.getTicketPrice());
            l.setFont(new Font("Arial", Font.BOLD, 20));
            rightP.add(l);

            JButton book = new JButton("Book");
            book.setBackground(new Color(0, 102, 0));
            book.setForeground(Color.WHITE);
            book.setBorderPainted(false);
            book.setFocusPainted(false);
            book.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e)
                {
                    book.setBackground(new Color(0, 51, 0));
                }
                public void mouseExited(MouseEvent e)
                {
                    book.setBackground(new Color(0, 102, 0));
                }
            });
            rightP.add(book);
            p.add(rightP, BorderLayout.EAST);

            book.addActionListener(e->
            {
                new BookingGui(f);
                dispose();
            });

            contentPanel.add(p);
        }

        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

}

class Booking implements Serializable
{
    private String mobileNo, email, firstName, surName, passportNo, nationality, ticketClass;
    private int travelers;
    private String bookingNumber;
    private FlightSchedule flight;

    public Booking(){}
    public Booking(String mobileNo,String email,String firstName,String surName,String passportNo,String nationality,String ticketClass, int travelers, FlightSchedule flight)
    {
        this.mobileNo=mobileNo;
        this.email=email;
        this.firstName=firstName;
        this.surName=surName;
        this.passportNo=passportNo;
        this.nationality=nationality;
        this.ticketClass=ticketClass;
        this.travelers=travelers;
        bookingNumber= "BK"+String.valueOf((int)(Math.random()*1000));
        this.flight=flight;
        
    }
    
    public void setEmail(String email) {
        this.email = email;
    }public void setFirstName(String firstName) {
        this.firstName = firstName;
    }public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }public void setNationality(String nationality) {
        this.nationality = nationality;
    }public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }public void setSurName(String surName) {
        this.surName = surName;
    }public void setTicketClass(String ticketClass) {
        this.ticketClass = ticketClass;
    }public void setTravelers(int travelers) {
        if (travelers <= 0) {
            throw new IllegalArgumentException("Number of travelers must be greater than 0.");
        }
        this.travelers = travelers;
    }public String getEmail() {
        return email;
    }public String getFirstName() {
        return firstName;
    }public String getMobileNo() {
        return mobileNo;
    }public String getNationality() {
        return nationality;
    }public String getPassportNo() {
        return passportNo;
    }public String getSurName() {
        return surName;
    }public String getTicketClass() {
        return ticketClass;
    }public int getTravelers() {
        return travelers;
    }public String getBookingNumber() {
        return bookingNumber;
    }public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }public FlightSchedule getFlight() {
        return flight;
    }public void setFlight(FlightSchedule flight) {
        this.flight = flight;
    }



    public void writeBooking(Booking booking)
    {
        try
        {
            ObjectOutputStream ous= new ObjectOutputStream(new FileOutputStream("bookings.dat", true));
            ous.writeObject(booking);
            ous.close();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Booking{" + "bookingNumber='" + bookingNumber + '\'' + ", mobileNo='" + mobileNo + '\''
                + ", email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", surName='" + surName + '\''
                + ", passportNo='" + passportNo + '\'' + ", nationality='" + nationality + '\'' + ", ticketClass='"
                + ticketClass + '\'' + ", travelers=" + travelers + ", flight=" + flight + '}';
    }
    

}

class BookingGui extends JFrame
{
    public BookingGui()
    {}
    public BookingGui(FlightSchedule flight)
    {
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Booking");
        setLayout(null);
        try{
                 JLabel image= new JLabel(new ImageIcon("image2.jpg"));
                    setContentPane(image);
        }
        catch(Exception j)
        {

        }
       
       
        

        JPanel titleJPanel= new JPanel();
        titleJPanel.setLayout(new BorderLayout());
        titleJPanel.setBounds(0, 0, 1200, 30);
       // titleJPanel.setPreferredSize(new Dimension(1200, 100));
        
        titleJPanel.setBackground(Color.BLUE);
        JLabel l= new JLabel("BOOKING");
        l.setFont(new Font("Arial", Font.BOLD, 28));
        l.setForeground(Color.WHITE);
        titleJPanel.add(l);
        JButton back= new JButton("Back");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e)
            {
                back.setBackground(Color.WHITE);
                back.setForeground(Color.BLACK);
            }
            public void mouseExited(MouseEvent e)
            {
                back.setBackground(Color.BLACK);
                back.setForeground(Color.WHITE);
            }
        });
        back.addActionListener(e->
        {
            new FlightScheduleGui();
            dispose();
        });
        titleJPanel.add(back, BorderLayout.EAST);
        add(titleJPanel);

        //getBooking(mainPanel);

        //setContentPane(mainPanel);
        getBooking(flight);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    String selected;
    public void getBooking(FlightSchedule flight) 
    {
        //JPanel p1 = new JPanel();
        //p1.setLayout(new GridLayout(4, 4, 2, 20)); 
        JLabel heading= new JLabel("CONTACT DETAILS:");
        heading.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 26));
        heading.setBounds(5, 50, 1000, 50);
        add(heading);

        JLabel l1 = new JLabel("Mobile No: ");
        l1.setFont(new Font("Arial", Font.BOLD, 18));
        l1.setBounds(5, 100, 100, 20);
        
        JTextField tf1 = new JTextField();
        tf1.setBounds(150, 100, 150, 30);
        add(l1);
        add(tf1);

        JLabel l2 = new JLabel("Email: ");
        l2.setFont(new Font("Arial", Font.BOLD, 18));
        l2.setBounds(450, 100, 100, 20);
        JTextField tf2 = new JTextField();
        tf2.setBounds(595, 100, 150, 30);
        add(l2);
        add(tf2);

        JLabel heading2= new JLabel("TRAVELERS DETAILS: ");
        heading2.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 26));      
        heading2.setBounds(5, 200, 1000, 50);
        add(heading2);


        JLabel l3 = new JLabel("First Name: ");
        l3.setFont(new Font("Arial", Font.BOLD, 18));
        l3.setBounds(5, 250, 150, 20);

        JTextField tf3 = new JTextField();
        tf3.setBounds(200, 250, 150, 30);
        add(l3);
        add(tf3);

        JLabel l4 = new JLabel("Sur Name: ");
        l4.setFont(new Font("Arial", Font.BOLD, 18));
        l4.setBounds(500, 250, 150, 20);

        JTextField tf4 = new JTextField();
        tf4.setBounds(645, 250, 150, 30);
        add(l4);
        add(tf4);

        JLabel l5 = new JLabel("Passport No: ");
        l5.setFont(new Font("Arial", Font.BOLD, 18));
        l5.setBounds(5, 300, 150, 20);
        add(l5);

        JTextField tf5 = new JTextField();
        tf5.setBounds(200, 300, 150, 30);
        add(tf5);

        JLabel l6 = new JLabel("Nationality: ");
        l6.setFont(new Font("Arial", Font.BOLD, 18));
        l6.setBounds(500, 300, 150, 20);
        add(l6);

        String[] countries = {
        "Pakistan","Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria",
        "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
        "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia",
        "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo", "Costa Rica",
        "Croatia", "Cuba", "Cyprus", "Czechia", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador",
        "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France",
        "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau",
        "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy",
        "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, North", "Korea, South", "Kosovo", "Kuwait",
        "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
        "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico",
        "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal",
        "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman","Pakistan", "Palau",
        "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda",
        "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
        "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
        "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria",
        "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
        "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu",
        "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
        };

        JComboBox<String> cb= new JComboBox<>(countries);
        cb.setBounds(640, 300, 150, 20);
        add(cb);

        JLabel l7= new JLabel("Ticket Class: ");
        l7.setFont(new Font("Arial", Font.BOLD, 18));
        l7.setBounds(5, 350, 150, 20);
        add(l7);

        

        JRadioButton rb1= new JRadioButton("Economy");
        rb1.setBounds(200, 350, 100, 20);
        rb1.addActionListener(e-> {
            selected=rb1.getText();
        });
        JRadioButton rb2= new JRadioButton("Business");
        rb2.setBounds(300, 350, 100, 20);
        rb2.addActionListener(e-> {
            selected=rb2.getText();
        });
        JRadioButton rb3= new JRadioButton("First");
        rb3.setBounds(400, 350, 75, 20);
        rb3.addActionListener(e-> {
            selected=rb3.getText();
        });
        ButtonGroup gb= new ButtonGroup();
        gb.add(rb1);gb.add(rb2);gb.add(rb3);
        
        add(rb1);add(rb2);add(rb3);


        JLabel l8 = new JLabel("Travelers: ");
        l8.setFont(new Font("Arial", Font.BOLD, 18));
        l8.setBounds(500, 350, 150, 20);
        add(l8);

      //  int noOfTravelers=1;
        SpinnerModel model = new SpinnerNumberModel(1,1,10,1);
        JSpinner spin= new JSpinner(model);
        spin.setBounds(640, 350, 100, 30);
        JFormattedTextField tf = ((JSpinner.DefaultEditor) spin.getEditor()).getTextField();
        tf.setEditable(false);
        
        add(spin);

        JButton confirm = new JButton("Confirm Booking");
        confirm.setFont(new Font("Arial", Font.PLAIN, 32));
        confirm.setForeground(Color.WHITE);
        confirm.setOpaque(false);
        confirm.setContentAreaFilled(false);
        confirm.setBorderPainted(false);
        confirm.setBounds(450, 450, 300,150);
        confirm.addMouseListener(new MouseAdapter() {
            
            public void mouseEntered(MouseEvent e)
            {
                confirm.setFont(new Font("Arial", Font.BOLD, 32));   
                confirm.setForeground(Color.GREEN);
            }
            public void mouseExited(MouseEvent e)
            {
                confirm.setFont(new Font("Arial", Font.PLAIN, 32));
                confirm.setForeground(Color.WHITE);
            }
        });
        

        

        

        confirm.addActionListener(e-> 
        {
            String mobileNo, email, firstName, surName, passportNo, nationality, ticketClass;
            int travelers;
            ticketClass=selected;
            

            mobileNo=tf1.getText(); email=tf2.getText(); firstName=tf3.getText(); surName=tf4.getText(); passportNo=tf5.getText(); nationality=(String)cb.getSelectedItem();
            travelers=(int)spin.getValue();
        
            

            if (mobileNo != null && email != null && passportNo != null && firstName != null && surName != null && nationality != null && ticketClass != null)
            {
                
                int choice=JOptionPane.showConfirmDialog(null, "Your ticket details are:\n \nDeparture: "+flight.getDeparture()+"\nDestination: "+flight.getDestination()+"\nTravelers: "+travelers+"\nClass: "+ticketClass+"\nTotal bill: "+(ticketClass.equals("Economy")?(flight.getTicketPrice()*travelers):ticketClass.equals("Business")?((flight.getTicketPrice()+(flight.getTicketPrice()/10))*travelers):((flight.getTicketPrice()+(flight.getTicketPrice()/30))*travelers))+"\n\nAre you sure you want to proceed?","Confirmation", JOptionPane.YES_NO_OPTION);
                if(choice==JOptionPane.YES_OPTION)
                {
                    Booking b= new Booking(mobileNo, email, firstName, surName, passportNo, nationality, ticketClass, travelers, flight);
                    b.writeBooking(b);
                    JOptionPane.showMessageDialog(null, "Your booking is confirmed");
                    new FlightScheduleGui();
                    dispose();
                }
            }
            
            else
            {
                JOptionPane.showMessageDialog(null, "Please fill all fields");
            }

            
        });

        add(confirm);



        //add(p1, BorderLayout.CENTER); 
    }

}


public class ProjectFlight 
{
    public static void main(String[] args) {
        FlightSchedule f1 = new FlightSchedule("lahore", "isb", "qatar airways", "2023/12/24/", "22:30", "4:00", 100, 0, "124");
        FlightSchedule f2 = new FlightSchedule("lahore", "isb", "hulu", "2023/12/24/", "22:30", "2:30", 99000, 0, "123");
        FlightSchedule f3 = new FlightSchedule("karachi", "isb", "qatar airways", "2023/12/24/", "22:30", "1:30", 115000, 1, "123");
        FlightSchedule f4 = new FlightSchedule("lahore", "isb", "hulu", "2023/12/24/", "0:30", "5:15", 99000, 2, "123");
        FlightSchedule f5 = new FlightSchedule("bangkok", "isb", "hulu", "2023/12/24/", "22:00", "4:00", 99000, 0, "123");
        FlightSchedule f6 = new FlightSchedule("tokyo", "isb", "hulu", "2023/12/24/", "22:00", "8:00", 99000, 0, "123");
        FlightSchedule f7 = new FlightSchedule("peshawar", "isb", "turkish airlines", "2023/12/24/", "18:30", "5:00", 99000, 0, "123");
        FlightSchedule f8 = new FlightSchedule("sialkot", "isb", "saudia", "2023/12/24/", "10:30", "22:00", 99000, 1, "123");
        FlightSchedule f9 = new FlightSchedule("dubai", "isb", "qatar airways", "2023/12/24/", "22:30", "3:00", 99000, 0, "123");
        FlightSchedule f10 = new FlightSchedule("abu dhabi", "isb", "hulu", "2023/12/24/", "2:30", "8:00", 99000, 1, "123");
        FlightSchedule f11 = new FlightSchedule("abu dhabi", "karachi", "hulu", "2023/12/24/", "2:30", "8:00", 99000, 1, "123");


        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream("flightschedule.dat"))) {
            ArrayList<FlightSchedule> schedulesList = new ArrayList<>();
            schedulesList.add(new FlightSchedule("lahore", "isb", "qatar airways", "2023/12/24/", "22:30", "4:00", 100, 0, "124"));
            schedulesList.add(new FlightSchedule("lahore", "isb", "hulu", "2023/12/24/", "22:30", "2:30", 99000, 0, "123"));
            schedulesList.add(f3);
            schedulesList.add(f4);
            schedulesList.add(f5);
            schedulesList.add(f6);
            schedulesList.add(f7);
            schedulesList.add(f8);
            schedulesList.add(f9);
            schedulesList.add(f10);
            schedulesList.add(f11);
            ous.writeObject(schedulesList);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        new MainMenuProject();
    }
}