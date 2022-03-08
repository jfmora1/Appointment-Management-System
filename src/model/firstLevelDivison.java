package model;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**First level division model class. */
public class firstLevelDivison {
    private int divisionID;
    private String division;
    private int countryID;
    private static ObservableList<firstLevelDivison> allDivisons = FXCollections.observableArrayList();

    public firstLevelDivison(int divisionID, String division, int countryID) {
        this.divisionID = divisionID;
        this.division = division;
        this.countryID = countryID;
    }

    static {
        init();
    }

    private static void init() {
        if(allDivisons.size() == 0){
            ObservableList<firstLevelDivison> allinfo = query.getDivionTable();
            allDivisons.addAll(allinfo);
        }
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public static ObservableList<firstLevelDivison> getAllDivisons(){
        return allDivisons;
    }

    @Override
    public  String toString(){
        return (division);
    }


}
