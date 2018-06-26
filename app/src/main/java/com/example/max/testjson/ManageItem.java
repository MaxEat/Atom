package com.example.max.testjson;

/**
 * Created by max on 2018/6/25.
 */

public class ManageItem extends Item {
    private int borrowNr;
    private int maintainNr;
    private int leftNr;

    public ManageItem() {
        borrowNr = 0;
        maintainNr = 0;
        leftNr = 0;
    }

    public int getLeftNr() {
        return leftNr;
    }

    public void setLeftNr(int LeftNr) {
        this.leftNr = LeftNr;
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

    public void increaseLeftNr() {
        leftNr++;
    }

    public void setMaintainNr(int maintainNr) {
        this.maintainNr = maintainNr;
    }
}
