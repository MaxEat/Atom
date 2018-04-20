package com.example.max.testjson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by max on 2018/4/18.
 */

public class CustomedWebview extends WebView {


    static final String baseURL = "https://labtools.groept.be/inventory/secure";
    static final String baseIndexURL = "https://labtools.groept.be/inventory/secure/";
    static final String registerPersonURL = "https://labtools.groept.be/inventory/secure/php_addPerson.php";
    static final String registerItemURL = "https://labtools.groept.be/inventory/secure/php_addItem.php";
    static final String getAllBorrowedItemsURL = "https://labtools.groept.be/inventory/secure/php_selectItemByUser.php";
    static final String borrowItemURL = "https://labtools.groept.be/inventory/secure/php_borrowItem.php";
    static final String returnItemURL = "https://labtools.groept.be/inventory/secure/php_returnItem.php";
    static final String duplicatePersonURL = "https://labtools.groept.be/inventory/secure/php_duplicatePerson.php";
    static final String getInfoByCardURL = "https://labtools.groept.be/inventory/secure/php_getUserInfoByCard.php";
    static final String getAllAvailableItemsURL = "https://labtools.groept.be/inventory/secure/php_selectAvailableItem.php";
    static final String addItemToWishListURL = "https://labtools.groept.be/inventory/secure/php_addItemToWish.php";
    static final String removeItemFromWishListURL = "https://labtools.groept.be/inventory/secure/php_removeItemFromWish.php";
    static final String checkItemAvailableURL = "https://labtools.groept.be/inventory/secure/php_selectAvailableItem.php";
    static final String getInfoByItemTagURL = "https://labtools.groept.be/inventory/secure/php_getItemInfoByTag.php";
    static final String getAllWishListItemsURL = "https://labtools.groept.be/inventory/secure/php_getWishItems.php";
    static final String updateItemStateUrl = "https://labtools.groept.be/inventory/secure/php_maintainItem.php";
    static final String getPictureNumberUrl = "https://labtools.groept.be/inventory/secure/php_getItemPictureNumber.php";
    static final String getAllClassificationsURL = "https://labtools.groept.be/inventory/secure/php_getAllClassifications.php";
    static final String getExpiredItemURL = "https://labtools.groept.be/inventory/secure/php_getExpiredItems.php";
    static final String addNewItemURL = "https://labtools.groept.be/inventory/secure/php_addItem.php";
    static final String UPLOAD_URL = "https://labtools.groept.be/inventory/secure/upload.php";
    static final String IMAGES_URL = "http://labtools.groept.be/inventory/secure/getImages.php";
    static final String getItemPictureURL = "https://labtools.groept.be/inventory/secure/getImages.php";



    public void hide() {
        setVisibility(View.GONE);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    @SuppressLint("JavascriptInterface")
    public void addInterface(Object object, String name) {
        addJavascriptInterface(object, name);
    }

    @SuppressLint("JavascriptInterface")
    CustomedWebview(Context context, AttributeSet attr) {
        super(context, attr);

        requestFocus();
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);


        setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
            @Override
            public void onPageFinished(WebView view, String url) {

                Log.i("current url", url);

                if(url.equals(baseIndexURL) || url.equals(baseURL))
                    view.loadUrl("javascript:window.local.getRegisterInfo(document.body.innerHTML)");

                if(url.equals(getAllBorrowedItemsURL ))
                    view.loadUrl("javascript:window.Person.getAllItem_Interface(document.body.innerHTML)");

                if(url.equals(borrowItemURL))
                    view.loadUrl("javascript:window.Person.borrowItem_Interface(document.body.innerHTML)");

                if(url.equals(returnItemURL))
                    view.loadUrl("javascript:window.Person.returnItem_Interface(document.body.innerHTML)");

                if(url.equals(registerPersonURL))
                    view.loadUrl("javascript:window.Person.registerPerson_interface(document.body.innerHTML)");

                if(url.equals(updateItemStateUrl))
                    view.loadUrl("javascript:window.Person.updateItemStatus_Interface(document.body.innerHTML)");

                if(url.equals(addNewItemURL))
                    view.loadUrl("javascript:window.Person.administratorAddItem_interface(document.body.innerHTML)");

                if(url.equals(getAllAvailableItemsURL))
                    view.loadUrl("javascript:window.AvailableItem.getAllAvailableItems_interface(document.body.innerHTML)");

                if(url.equals(getInfoByItemTagURL))
                    view.loadUrl("javascript:window.Item.setInfos_interface(document.body.innerHTML)");


                if(url.equals(getPictureNumberUrl))
                    view.loadUrl("javascript:window.Item.setImageFromDatabase_interface(document.body.innerHTML)");



//                if(url.equals(getInfoByCardURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//
//
//
//                if(url.equals(addItemToWishListURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//
//                if(url.equals(removeItemFromWishListURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//
//                if(url.equals(checkItemAvailableURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//
//
//                if(url.equals(getAllWishListItemsURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");


            }

        });
    }
}
