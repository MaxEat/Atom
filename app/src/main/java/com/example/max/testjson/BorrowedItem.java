package com.example.max.testjson;

/**
 * Created by max on 2018/4/10.
 */

public class BorrowedItem extends Item {


    static int number = 0;
    int id;
    String borrowedTimeStamp;
    String borrwedLocation;


    public String getBorrowedTimeStamp() {
        return borrowedTimeStamp;
    }

    public String getBorrwedLocation() {
        return borrwedLocation;
    }


    BorrowedItem() {
        super();
    }

    BorrowedItem(String ItemTag) {
        super(ItemTag);
    }

    BorrowedItem(String aitemTag, String aitemLocation) {
        super(aitemTag, aitemLocation);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = number;
        number++;
    }

    public void setBorrowedTimeStamp(String borrowedTimeStamp) {
        this.borrowedTimeStamp = borrowedTimeStamp;
    }

    public void setBorrwedLocation(String borrwedLocation) {
        this.borrwedLocation = borrwedLocation;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Location=" + borrwedLocation +
                ", BoughTime=" + borrowedTimeStamp +
                '}';
    }

}
