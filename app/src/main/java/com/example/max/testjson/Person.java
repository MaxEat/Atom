package com.example.max.testjson;

/**
 * Created by max on 2018/4/5.
 */

public class Person {
    private String kuleuvenID;
    private String cardID;
    private String email;
    private int userType;
    private String userName;

    Person(String aCardID) {
        cardID = aCardID;

    }

    Person(String aUserName, String aKuleuvenID, String Email ) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        userType = 2;
    }



    public void setCardID(String CardID) {
        cardID = CardID;
    }

    public String getKuleuvenID() {
        return kuleuvenID;
    }

    public String getCardID() {
        return cardID;
    }

    public String getEmail() {
        return email;
    }

    public int getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

    public void register() {
        BackgroundTask.addPerson(this);
    }

    public void getItem() {
        BackgroundTask.receiveItems(this);
    }

    public void borrowItem(Item item) {
        BackgroundTask.borrowItem(this, item);
    }

    public void returnItem(Item item) {
        BackgroundTask.returnItem(this, item);
    }

}
