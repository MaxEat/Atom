package com.example.max.testjson;

/**
 * Created by max on 2018/4/5.
 */

public class Item {
    private String itemTag;
    private String itemLocation;
    private String boughtTime;
    private int itemPermission;


    private static BackgroundTask backgroundTask = new BackgroundTask();

    public String getItemTag() {
        return itemTag;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public int getItemPermission() {
        return itemPermission;
    }


    Item( String ItemTag) {
        itemTag = ItemTag;
    }

    public void registerItem() {
        backgroundTask.addItem(this);

    }



}
