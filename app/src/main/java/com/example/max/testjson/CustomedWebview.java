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
    static final String getPermissionClassificationURL = "https://labtools.groept.be/inventory/secure/php_getClassificationPermission.php";
    static final String getExpiredItemURL = "https://labtools.groept.be/inventory/secure/php_returnExpirationInfo.php";
    static final String addNewItemURL1 = "https://labtools.groept.be/inventory/secure/php_addItem1.php";
    static final String addNewItemURL2 = "https://labtools.groept.be/inventory/secure/php_addItem2.php";
    static final String getAllPermissionsURL = "https://labtools.groept.be/inventory/secure/php_getAllPermissions.php";
    static final String UPLOAD_URL = "https://labtools.groept.be/inventory/secure/upload.php";
    static final String IMAGES_URL = "http://labtools.groept.be/inventory/secure/getImages.php";
    static final String getItemPictureURL = "https://labtools.groept.be/inventory/secure/getImages.php";
    static final String changeBlackListStateURL = "https://labtools.groept.be/inventory/secure/removeFromBlackList.php";
    static final String updateAlertEmail = "https://labtools.groept.be/inventory/secure/php_updateEmail.php";
    static final String getItemsOfSameKindURL = "https://labtools.groept.be/inventory/secure/php_sameKindItems.php";
    static final String getPersonalItemsURL = "https://labtools.groept.be/inventory/secure/php_getPersonalItems.php";
    static final String initializeWorkerURL = "https://labtools.groept.be/inventory/secure/php_initializeWorker.php";



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

                if(url.equals(duplicatePersonURL))
                    view.loadUrl("javascript:window.local.checkPersonInfo(document.body.innerHTML)");


                if(url.equals(getAllBorrowedItemsURL ))
                    view.loadUrl("javascript:window.Person.getAllItem_Interface(document.body.innerHTML)");

                if(url.equals(borrowItemURL))
                    view.loadUrl("javascript:window.Person.borrowItem_Interface(document.body.innerHTML)");

                if(url.equals(returnItemURL))
                    view.loadUrl("javascript:window.Person.returnItem_Interface(document.body.innerHTML)");

                if(url.equals(registerPersonURL))
                    view.loadUrl("javascript:window.registerPerson.registerPerson_interface(document.body.innerHTML)");

                if(url.equals(updateItemStateUrl))
                    view.loadUrl("javascript:window.Person.updateItemStatus_Interface(document.body.innerHTML)");

                if(url.equals(addNewItemURL1))
                    view.loadUrl("javascript:window.Person.administratorAddItem_interface(document.body.innerHTML)");

                if(url.equals(addNewItemURL2))
                    view.loadUrl("javascript:window.Person.administratorAddItemNew_interface(document.body.innerHTML)");

                if(url.equals(getAllAvailableItemsURL))
                    view.loadUrl("javascript:window.Person.getAllAvailableItems_interface(document.body.innerHTML)");

                if(url.equals(getPersonalItemsURL))
                    view.loadUrl("javascript:window.Person.getPersonalItems_interface(document.body.innerHTML)");

                if(url.equals(initializeWorkerURL))
                    view.loadUrl("javascript:window.Person.initializeWorker_interface(document.body.innerHTML)");


                if(url.equals(getItemsOfSameKindURL))
                    view.loadUrl("javascript:window.Person.getItemsSameKind_interface(document.body.innerHTML)");

                if(url.equals(getExpiredItemURL))
                    view.loadUrl("javascript:window.Person.getExpiredItemPersonDatabase_interface(document.body.innerHTML)");

                if(url.equals(changeBlackListStateURL))
                    view.loadUrl("javascript:window.Person.changeBlacklist_interface(document.body.innerHTML)");

                if(url.equals(getPermissionClassificationURL))
                    view.loadUrl("javascript:window.local.getPermissionClassification_interface(document.body.innerHTML)");

                if(url.equals(updateAlertEmail))
                    view.loadUrl("javascript:window.Person.updateEmail_interface(document.body.innerHTML)");

                if(url.equals(getInfoByItemTagURL))
                    view.loadUrl("javascript:window.Item.setInfos_interface(document.body.innerHTML)");

                if(url.equals(getPictureNumberUrl))
                    view.loadUrl("javascript:window.Item.setImageFromDatabase_interface(document.body.innerHTML)");

                if(url.equals(addItemToWishListURL))
                    view.loadUrl("javascript:window.Person.addItemToWish_interface(document.body.innerHTML)");

                if(url.equals(removeItemFromWishListURL))
                    view.loadUrl("javascript:window.Person.removeItemFromWish_interface(document.body.innerHTML)");

                if(url.equals(getAllWishListItemsURL))
                    view.loadUrl("javascript:window.Person.getWishListFromDatabase_interface(document.body.innerHTML)");


//                if(url.equals(getInfoByCardURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//

//
//                if(url.equals(checkItemAvailableURL))
//                    view.loadUrl("javascript:window.local_obj.parseBorrowItem(document.body.innerHTML)");
//
//


            }

        });
    }
}
