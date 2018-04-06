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
    private static BackgroundTask backgroundTask = new BackgroundTask();

    Person(String cardID) {


        

    }
    Person(String aUserName, String aKuleuvenID, String Email ) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        userType = 2;
    }


        Person2(String aUserName, String aKuleuvenID, String Email ) {
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
        backgroundTask.addPerson(this);
    }

    public void getItem() {
        backgroundTask.receiveItems(this);
    }

//    public static Person getPersonByCardID(String cardID) {
//
//    }

    public void borrowItem(Item item) {
        backgroundTask.statusChangeItem(this, item, 1);
    }

    public void returnItem(Item item) {
        backgroundTask.statusChangeItem(this, item, 2);
    }

}
