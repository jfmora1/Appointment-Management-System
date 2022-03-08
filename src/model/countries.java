package model;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Countries model class. */
public class countries {

    private int countryID;
    private String country;
    private static ObservableList<countries> allCountries = FXCollections.observableArrayList();

    public countries(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }

    static {
        init();
    }

    private static void init() {
        if(allCountries.size() == 0){
            ObservableList<countries> allinfo = query.getCountryTable();
            allCountries.addAll(allinfo);
        }
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static ObservableList<countries> getAllCountries(){
        return allCountries;
    }

    @Override
    public  String toString(){
        return (country);
    }
}
