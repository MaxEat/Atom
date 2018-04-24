package com.example.max.testjson;

import android.util.Log;
import android.webkit.JavascriptInterface;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/10.
 */

public class AvailableItem extends Item {


    private int quantity;
    private int id;

    AvailableItem() {}

    AvailableItem(String aitemTag, String aitemLocation) {
        super(aitemTag,aitemLocation);
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

    @Override
    public String toString() {
        return "Item{" +
                ", Tag=" + getItemTag() +
                ", Id=" + getId() +
                ", Location=" + getItemLocation() +
                ", Classfication=" + getClassification() +
                '}';
    }
}
