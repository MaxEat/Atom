package com.example.max.testjson;

/**
 * Created by max on 2018/4/5.
 */

public class Item {
    private String itemTag;
    private String itemLocation;
    private String boughtTime;
    private int itemPermission;

    Item( String ItemTag) {
        itemTag = ItemTag;
    }

    Item(String aitemTag, String aboughtTime, String aitemLocation) {
            itemTag = aitemTag;
            boughtTime = aboughtTime;
            itemLocation = aitemLocation;
            itemPermission = 1;
    }

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




    public void register() {
        BackgroundTask.addItem(this);

    }



}
