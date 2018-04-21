package com.example.max.testjson;

/**
 * Created by ASUS on 2018/4/20.
 */

public class Admin_AvailableItem extends Item {


    private int quantity;
    private int id;

    Admin_AvailableItem() {}

    Admin_AvailableItem(String aitemTag, String aitemLocation) {
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




//
//    public static void getAllAvailableItems() {
//        JSONObject postdata = new JSONObject();
//
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllAvailableItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success get available", "result----" + result);
//                    try {
//                        availableItems = new ArrayList<AvailableItem>();
//                        availableItemMap = new HashMap<String, AvailableItem>();
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            AvailableItem item = new AvailableItem(json.getString("itemTag"), json.getString("itemLocation"));
//                            item.setClassification(json.getString("itemClassification"));
//                            item.setId(i);
//                            availableItems.add(item);
//                            availableItemMap.put(Integer.toString(item.getId()), item);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailture() {
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

