package com.example.max.testjson;

import android.icu.util.Calendar;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.zxing.client.result.CalendarParsedResult;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max on 2018/4/10.
 */

public class BorrowedItem extends Item implements Serializable{


    static int number = 0;
    private int id;
    private String borrowedTimeStamp;
    private String borrowedLocation;
    private String returnDate;
    private long leftDays;
    private long allowableDays;

    public String getBorrowedTimeStamp() {
        return borrowedTimeStamp;
    }

    public String getBorrowedLocation() {
        return borrowedLocation;
    }

    BorrowedItem(String ItemTag) {
        super(ItemTag);
    }

    BorrowedItem(String aitemTag, String aitemLocation) {
        super(aitemTag, aitemLocation);
        borrowedLocation = aitemLocation;
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        borrowedTimeStamp = format.format(currentTime.getTime());

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

    public void setBorrowedLocation(String borrowedLocation) {
        this.borrowedLocation = borrowedLocation;
    }

    public void calculateAllowableDays(Person person) {
        Log.i("user type", person.getUserType());
        if(person.getUserType().equals("Student"))
            allowableDays = TestJson.permission_days_student.get(getClassification());
        else
            allowableDays = TestJson.permission_days_worker.get(getClassification());

        String dt = borrowedTimeStamp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE,(int)allowableDays);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        returnDate = sdf1.format(c.getTime());
        Log.i("return Date", returnDate);
    }

    public void setLeftDays() {

        calculateAllowableDays(TestJson.getUser());
        Date currentTime = Calendar.getInstance().getTime();
        Date returnTime = Calendar.getInstance().getTime();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            returnTime = format.parse(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff =  returnTime.getTime() - currentTime.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        leftDays = hours / 24;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getLeftDays() {
        return Long.toString(leftDays);
    }

    public long getLeftDaysNr() {
        return leftDays;
    }
    public long getAllowableDays() {
        return allowableDays;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Location = " + borrowedLocation +
                ", borrowedTimeStamp = " + borrowedTimeStamp +
                ", should be returned at " + returnDate +
                ", allowable Days = " + allowableDays +
                ", leftDays = " + leftDays +
                '}';
    }

}
