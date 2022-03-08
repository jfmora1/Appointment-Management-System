package model;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Customer model class. */
public class customer {
    private int divisionID;
    private int customerID;
    private String customerName;
    private String address;
    private String postalcode;
    private String phoneNumber;
    private String firstlevelDivision;
    private String country;
    private static ObservableList<customer> allCustomers = FXCollections.observableArrayList();

    public customer(int customerID, String customerName, String address, String postalcode, String phoneNumber, String firstlevelDivision,int divisionID, String country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.postalcode = postalcode;
        this.phoneNumber = phoneNumber;
        this.firstlevelDivision = firstlevelDivision;
        this.divisionID = divisionID;
        this.country = country;
    }
    static {
        init();
    }

    private static void init(){
        if(allCustomers.size() == 0){
            ObservableList<customer> allData = query.getAllCustomerinfo();
            allCustomers.addAll(allData);
        }
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstlevelDivision() {
        return firstlevelDivision;
    }

    public void setFirstlevelDivision(String firstlevelDivision) {
        this.firstlevelDivision = firstlevelDivision;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static ObservableList<customer> getAllCustomers(){
        return allCustomers;
    }

    @Override
    public  String toString(){
        return (customerName);
    }

//    public static boolean deleteCustomer(customer deleteCustomer) {
//        if(allCustomers.contains(deleteCustomer)){
//            allCustomers.remove(deleteCustomer);
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    public static boolean updateCustomer(customer updateCustomer){
//        if(allCustomers.contains(updateCustomer)){
//            allCustomers.addAll(updateCustomer);
//            return true;
//        }else {
//            return false;
//        }
//    }



}
