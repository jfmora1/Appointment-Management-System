package model;

import helper.query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Contacts model class. */
public class contacts {
    private int contact_id;
    private String contact_name;
    private String email;
    private static ObservableList<contacts> all_contacts = FXCollections.observableArrayList();

    public contacts(int contact_id, String contact_name, String email) {
        this.contact_id = contact_id;
        this.contact_name = contact_name;
        this.email = email;
    }

    static {
        init();
    }

    private static void init() {
        if(all_contacts.size() == 0){
            ObservableList<contacts> allinfo = query.getContactInfo();
            all_contacts.addAll(allinfo);
        }
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ObservableList<contacts> getAll_contacts() {
        return all_contacts;
    }

    @Override
    public  String toString(){
        return (contact_name); 
    }

}
