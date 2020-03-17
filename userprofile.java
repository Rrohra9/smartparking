package com.example.aaokabhiparkkarne;

public class userprofile {
    String userID;
    String plateno;
    String bookedslot;

    public userprofile(){//used while reading the values

    }

    public userprofile(String userID, String plateno, String bookedslot){ //to initialise our variables
        this.userID=userID;
        this.plateno = plateno;
        this.bookedslot = bookedslot;
    }

    public String getPlateno() { //getter are used to read the values
        return plateno;
    }

    public String getBookedslot() {
        return bookedslot;
    }
    public String getUserID(){
        return userID;
    }
}
