package com.example.max.testjson;

/**
 * Created by max on 2018/4/10.
 */

public class AvailableItem extends Item {


    private int quantity;
    private int id;
    public static int static_id = 0;
    private boolean inWishList;

    AvailableItem() {
        id = static_id;
        static_id++;
        quantity = 1;
        inWishList = false;
    }

    AvailableItem(String aitemTag, String aitemLocation) {
        super(aitemTag,aitemLocation);
        id = static_id;
        static_id++;
        quantity = 1;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setInWishList( boolean Wish){
        inWishList = Wish;
    }

    public boolean getInWishList() {
        return inWishList;
    }

    @Override
    public String toString() {
        return "Item{ Location=" + getItemLocation() +
                ", Classfication=" + getClassification() +
                ", Quantity=" + getQuantity() +
                '}';
    }
}
