package helper;

import controller.alertMessages;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Main;
import model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
/**Abstract query class. */
public abstract class query {

    private static boolean successfulLogin = false;
    public static boolean getSuccessLogin(){return successfulLogin;}
    public static void setSuccessfulLogin(boolean successfulLogin) {
        query.successfulLogin = successfulLogin;
    }
    private static String userLoggedIn;
    public static String getUserLoggedIn(){
        return userLoggedIn;
    }

    /**Get contact info method.
     * @return clist returns customers in database*/
    public static ObservableList<contacts> getContactInfo() {
        ObservableList<contacts> clist = FXCollections.observableArrayList();
        try {

            String sql = "Select * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                contacts C = new contacts(contactID, contactName, email);
                clist.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return clist;
    }

    /**Get country table method.
     * Grabs the countries in the database. */
    public static ObservableList<countries> getCountryTable(){
        ObservableList<countries> countryList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                countries C = new countries(countryID,countryName);
                countryList.add(C);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return countryList;
    }

    public static ObservableList<firstLevelDivison> getDivionTable(){
        ObservableList<firstLevelDivison> divisionList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM first_level_divisions;";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
            int divionID = rs.getInt("Division_ID");
            String divionName = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");
            firstLevelDivison fl = new firstLevelDivison(divionID,divionName,countryID);
            divisionList.add(fl);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionList;
    }

    public static ObservableList<users> getUserName(){
        ObservableList userinfo = FXCollections.observableArrayList();

        try {
            String sql = "SELECT User_Name,User_ID FROM users;";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String user = rs.getString("User_Name");
                int id = rs.getInt("User_ID");
                users u = new users(id,user);
                userinfo.add(u);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userinfo;
    }
    /**Get appointment by month method.
     * Grabs appointments by specific months.
     * @return filterapp returns observable list of appointments by month*/
    public static ObservableList<appointment> getAppointmentByMonth() throws SQLException {
        ObservableList<appointment> filterapp = FXCollections.observableArrayList();
        String sql = "SELECT monthname(Start) AS Month,count(Start) AS Total FROM appointments GROUP BY month";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String month = rs.getString("Month");
            int total = rs.getInt("Total");
            appointment app = new appointment(month,total);
            filterapp.add(app);
        }
        return filterapp;
    }

    public static ObservableList<appointment> getAppointmentByType() throws SQLException {
        ObservableList<appointment> filterApp = FXCollections.observableArrayList();
        String sql = "SELECT monthname(Start) AS Month,Type, count(Type) AS Total FROM appointments GROUP BY Type ";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String month = rs.getString("Month");
            String type = rs.getString("Type");
            int total = rs.getInt("Total");
            appointment app = new appointment(month,type,total);
            filterApp.add(app);
        }
        return filterApp;
    }

    public static ObservableList<appointment> getFilteredAppointments(ZonedDateTime start, ZonedDateTime end) throws SQLException {
        ObservableList<appointment> filteredApp = FXCollections.observableArrayList();
        Timestamp t1 = Timestamp.valueOf(start.toLocalDateTime());
        Timestamp t2 = Timestamp.valueOf(end.toLocalDateTime());

        String sql = "SELECT * FROM appointments inner join " +
                "customers ON appointments.Customer_ID=customers.Customer_ID " +
                "inner join " +
                "contacts ON appointments.Contact_ID=contacts.Contact_ID " +
                "WHERE Start BETWEEN ? AND ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setTimestamp(1,t1);
        ps.setTimestamp(2,t2);

        ResultSet rs = ps.executeQuery();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contact = rs.getString("Contact_Name");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            LocalDateTime ldt = startTime.toLocalDateTime();
            ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
            ZonedDateTime zdtlocal = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            LocalDateTime ldtlocal = zdtlocal.toLocalDateTime();
            String s = format.format(ldtlocal);

            LocalDateTime endltd = endTime.toLocalDateTime();
            ZonedDateTime endzdt = endltd.atZone(ZoneId.of("UTC"));
            ZonedDateTime endzdtlocal = endzdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            LocalDateTime endldtlocal = endzdtlocal.toLocalDateTime();
            String e = format.format(endldtlocal);

            appointment app = new appointment(appointmentID, title, description, location,type, contact, s, e, customerID, userID);
            filteredApp.add(app);
        }
        return filteredApp;
    }

    /**Get app15min method.
     * Grabs appointments within a 15 minute window timeframe.
     * @param user user logged in*/
    public static ObservableList<appointment> getApp15Min(int user) throws SQLException {
        ObservableList<appointment> filterapp = FXCollections.observableArrayList();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zdt = now.atZone(ZoneId.systemDefault());
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldtplus15 = ldt.plusMinutes(15);
        Timestamp t1 = Timestamp.valueOf(ldt);
        Timestamp t2 = Timestamp.valueOf(ldtplus15);

        String sql = "SELECT * FROM appointments WHERE Start BETWEEN ? AND ? AND User_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setTimestamp(1,t1);
        ps.setTimestamp(2,t2);
        ps.setInt(3,user);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contact = rs.getString("Contact_ID");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            LocalDateTime ldtb = startTime.toLocalDateTime();
            ZonedDateTime zdtb = ldtb.atZone(ZoneId.of("UTC"));
            ZonedDateTime zdtlocal = zdtb.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            LocalDateTime ldtlocal = zdtlocal.toLocalDateTime();
            String s = format.format(ldtlocal);

            LocalDateTime endltd = endTime.toLocalDateTime();
            ZonedDateTime endzdt = endltd.atZone(ZoneId.of("UTC"));
            ZonedDateTime endzdtlocal = endzdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
            LocalDateTime endldtlocal = endzdtlocal.toLocalDateTime();
            String e = format.format(endldtlocal);

            appointment app = new appointment(appointmentID, title, description, location,type, contact, s, e, customerID, userID);
            System.out.println("SUCCESSFULLY FOUND APPOINTMENT IN 15 MINUTES");
            filterapp.add(app);
        }
        return filterapp;
    }

    public static ObservableList<appointment> getAllAppointmentInfo() {
        ObservableList appointmentInfo = FXCollections.observableArrayList();

        try {
            String sql = "SELECT DISTINCT * FROM appointments inner join " +
                    "customers ON appointments.Customer_ID=customers.Customer_ID " +
                    "inner join " +
                    "contacts ON appointments.Contact_ID=contacts.Contact_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                String contact = rs.getString("Contact_Name");
                Timestamp startTime = rs.getTimestamp("Start");
                Timestamp endTime = rs.getTimestamp("End");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");



                //Formatting Timestamp to UTC and then localtime zone and then localdatetime
                LocalDateTime ldt = startTime.toLocalDateTime();
                ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime zdtlocal = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                LocalDateTime ldtlocal = zdtlocal.toLocalDateTime();
                String s = format.format(ldtlocal);


                LocalDateTime endltd = endTime.toLocalDateTime();
                ZonedDateTime endzdt = endltd.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdtlocal = endzdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
                LocalDateTime endldtlocal = endzdtlocal.toLocalDateTime();
                String e = format.format(endldtlocal);

                appointment app = new appointment(appointmentID, title, description, location,type, contact, s, e, customerID, userID);
                appointmentInfo.add(app);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentInfo;
    }
    /**Get all customer info method.
     * Grabs all customer information. */
    public static ObservableList<customer> getAllCustomerinfo() {
        ObservableList customerInfo = FXCollections.observableArrayList();

        try {
            String sql = "SELECT DISTINCT * FROM customers\n" +
                    "inner join first_level_divisions ON customers.Division_ID=first_level_divisions.Division_ID\n" +
                    "inner join countries ON first_level_divisions.Country_ID=countries.Country_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                String division = rs.getString("Division");
                int divisionID = rs.getInt("Division_ID");
                String country = rs.getString("Country");
                customer cm = new customer(customerID, customerName, address, postalCode, phoneNumber, division, divisionID, country);
                customerInfo.add(cm);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customerInfo;
    }

    public static ObservableList<appointment> getOverlapAppointments(int customerID, LocalDateTime appointmentDate) throws SQLException {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Timestamp appDate = Timestamp.valueOf(appointmentDate);
        ObservableList<appointment> overlapAppointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments WHERE Start = ? AND Customer_ID = ?";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setTimestamp(1,appDate);
        ps.setInt(2,customerID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String contact = rs.getString("Contact_ID");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            int cID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            //Formatting Timestamp to UTC and then localtime zone and then localdatetime
            LocalDateTime ldt = startTime.toLocalDateTime();
//            ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
//            ZonedDateTime zdtlocal = zdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
//            LocalDateTime ldtlocal = zdtlocal.toLocalDateTime();
            String s = format.format(ldt);


            LocalDateTime endltd = endTime.toLocalDateTime();
//            ZonedDateTime endzdt = endltd.atZone(ZoneId.of("UTC"));
//            ZonedDateTime endzdtlocal = endzdt.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
//            LocalDateTime endldtlocal = endzdtlocal.toLocalDateTime();
            String e = format.format(endltd);

            appointment a = new appointment(appointmentID, title, description, location,type, contact, s, e, cID, userID);
            overlapAppointments.add(a);

        }
        return overlapAppointments;
    }

    public static void loginUser(String username, String password) throws IOException {
        Main m = new Main();

        //SETUP FOR LOGIN ACTIVITY TO TXT FILE
        String filename = "login_activity.txt";
        FileWriter fwriter = new FileWriter(filename,true);
        PrintWriter pwriter = new PrintWriter(fwriter);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String sql = "SELECT Password FROM users WHERE User_Name = ?";
            ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("User not found in DB");
                alertMessages.invalidUsername();
                pwriter.println("Username: "+username +" | " +" unsuccessful login attempt on " + format.format(now) +" " + zone);
            } else {
                while (rs.next()) {
                    String retrievePassword = rs.getString("Password");
                    if (retrievePassword.equals(password)) {
                        userLoggedIn = username;
                        successfulLogin = true;
                        m.changeScene("/view/mainScreen.fxml");
                        pwriter.println("Username: "+username +" | " +" successful login attempt on " + format.format(now) +" " + zone);
                    } else {
                        System.out.println("Password does not match!");
                        alertMessages.invalidPassword();
                        pwriter.println("Username: "+username +" | " +" unsuccessful login attempt on " + format.format(now) +" " + zone);
                    }
                }

            }

        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        pwriter.close();
    }
    /**Insert customer method.
     * Inserts customers into the database. */
    public static int insertCustomer(String customerName, String address, String postalCode, String phone, int divisionID) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.setString(1,customerName);
        ps.setString(2,address);
        ps.setString(3,postalCode);
        ps.setString(4,phone);
        ps.setInt(5,divisionID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int updateCustomer(int customerID, String customerName, String address,String postalCode, String phone, int divisionID ) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?,Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?  WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,customerName);
        ps.setString(2,address);
        ps.setString(3,postalCode);
        ps.setString(4,phone);
        ps.setInt(5,divisionID);
        ps.setInt(6,customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,customerID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int deleteAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1,appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int insertAppointment(String title, String description, String location, String type, int customer_id, int user_id, int contact_id, ZonedDateTime start, ZonedDateTime end) throws SQLException {
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Customer_ID, User_ID, Contact_ID,Start,End) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";

        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String startString = start.format(format).toString();
//        String endString = end.format(format).toString();
        LocalDateTime startString = start.toLocalDateTime();
        LocalDateTime endString = end.toLocalDateTime();
        Timestamp s = Timestamp.valueOf(startString);
        Timestamp e = Timestamp.valueOf(endString);

        ps.setString(1,title);
        ps.setString(2,description);
        ps.setString(3,location);
        ps.setString(4,type);
        ps.setInt(5,customer_id);
        ps.setInt(6,user_id);
        ps.setInt(7,contact_id);
        ps.setTimestamp(8,s);
        ps.setTimestamp(9,e);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    public static int updateAppointment(int appointmentID, String title,String description, String location, String type,Timestamp start, Timestamp end,int customer_id,int user_id, int contact_id) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2,description);
        ps.setString(3,location);
        ps.setString(4,type);
        ps.setTimestamp(5,start);
        ps.setTimestamp(6,end);
        ps.setInt(7,customer_id);
        ps.setInt(8,user_id);
        ps.setInt(9,contact_id);
        ps.setInt(10,appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

}
