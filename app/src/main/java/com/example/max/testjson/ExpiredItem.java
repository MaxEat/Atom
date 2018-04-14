package com.example.max.testjson;

/**
 * Created by max on 2018/4/14.
 */

public class ExpiredItem extends BorrowedItem {
    private String borrowPersonName;
    private String borrowPersonEmail;
    private String borrowPersonID;
    private String itemTag;

    ExpiredItem(String item) {
        super(item);
    }

    public String getBorrowPersonName() {
        return borrowPersonName;
    }

    public String getBorrowPersonEmail() {
        return borrowPersonEmail;
    }

    public String getBorrowPersonID() {
        return borrowPersonID;
    }

    public void setBorrowPersonName(String borrowPersonName) {
        this.borrowPersonName = borrowPersonName;
    }

    public void setBorrowPersonEmail(String borrowPersonEmail) {
        this.borrowPersonEmail = borrowPersonEmail;
    }

    public void setBorrowPersonID(String borrowPersonID) {
        this.borrowPersonID = borrowPersonID;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }
}
