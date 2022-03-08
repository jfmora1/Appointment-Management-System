package model;
/**Users model class. */
public class users {
    private int user_id;
    private String userName;
    private String userPassword;

    public users(int user_id,String userName, String userPassword) {
        this.user_id = user_id;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public users(int user_id, String userName){
        this.user_id = user_id;
        this.userName =userName;
    }

    public users(String userName){
        this.userName = userName;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public  String toString(){
        return (userName);
    }
}
