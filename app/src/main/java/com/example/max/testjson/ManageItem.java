package com.example.max.testjson;

/**
 * Created by max on 2018/6/25.
 */

public class ManageItem extends Item {
    private int borrowNr;
    private int maintainNr;

    public ManageItem() {
        borrowNr = 0;
        maintainNr = 0;
    }

    public int getBorrowNr() {
        return borrowNr;
    }

    public int getMaintainNr() {
        return maintainNr;
    }

    public void setBorrowNr(int borrowNr) {
        this.borrowNr = borrowNr;
    }

    public void increaseBorrwoNr() {
        borrowNr++;
    }

    public void increaseMaintainNr() {
        maintainNr++;
    }

    public void setMaintainNr(int maintainNr) {
        this.maintainNr = maintainNr;
    }
}
