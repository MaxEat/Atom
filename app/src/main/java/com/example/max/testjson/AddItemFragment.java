package com.example.max.testjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.max.testjson.TestJson.wv;

public class AddItemFragment extends Fragment implements LocationListener{

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static Handler handler;
    private LocationManager locationManager;
    private Button QR;
    private Button BarCode;
    private Button TextRecognize;
    private Button Return_UnMaintain;
    private Button Borrow_Maintain;
    private TextView Classification;
    private ImageView ItemImage;

    private String provider;
    private String cityName;
    private String fullAddress;

    private String latitude;
    private String longitude;
    private EditText ScannedCode;
    private ImageButton ConfirmScan;
    private String itemTag;
    private String codeFormat;
    private String codeContent;
    private final String noResultErrorMsg = "No scan data received!";
    private static int dataGenerator;

    public AddItemFragment() {
    }

    public static AddItemFragment newInstance(int aDataGenerator) {
        AddItemFragment fragment = new AddItemFragment();
        dataGenerator = aDataGenerator;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkLocationPermission();

        locationManager = (LocationManager) getActivity().
                getSystemService(Context.LOCATION_SERVICE);

        longitude = "Longitude: " + locationManager.getLastKnownLocation(
                LocationManager.NETWORK_PROVIDER).getLongitude();
        latitude = "Latitude: " + locationManager.getLastKnownLocation(
                LocationManager.NETWORK_PROVIDER).getLatitude();
        Log.i("coordinate", longitude + ", " + latitude);

        Geocoder gcd = new Geocoder(getContext(), Locale.ENGLISH);
        List<Address> addresses;
        cityName = null;
        fullAddress = null;
        try {

            addresses = gcd.getFromLocation(
                    locationManager.getLastKnownLocation(
                            LocationManager.NETWORK_PROVIDER).getLatitude(),
                    locationManager.getLastKnownLocation(
                            LocationManager.NETWORK_PROVIDER).getLongitude(),
                    1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                fullAddress = addresses.get(0).getAddressLine(0);
                fullAddress = fullAddress.split(",")[0];
                fullAddress = fullAddress.concat(", Leuven");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        QR = (Button)view.findViewById(R.id.QR_scan);
        BarCode = (Button)view.findViewById(R.id.Barcode_scan);
        TextRecognize = (Button) view.findViewById(R.id.Text_scan);
        Return_UnMaintain = (Button) view.findViewById(R.id.return_item);
        Borrow_Maintain = (Button) view.findViewById(R.id.borrow_item);
        Classification = (TextView) view.findViewById(R.id.classificationAdditem);

        ItemImage = (ImageView)view.findViewById(R.id.itemImage);
        ScannedCode = (EditText)view.findViewById(R.id.scannedCode) ;
        ScannedCode.setText("");
        ConfirmScan = (ImageButton)view.findViewById(R.id.confirm);


        if(dataGenerator == 1){
            Return_UnMaintain.setText("RETURN");
            Borrow_Maintain.setText("BORROW");
        }
        else{
            Return_UnMaintain.setText("UnMaintain");
            Borrow_Maintain.setText("Maintain");
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                switch (inputMessage.what) {
                    case 3:
                        Log.i("ui handler", "set item info");

                        String classification = inputMessage.getData().getString("classification");
                        String pictureUrl = TestJson.pictureMap.get(classification);
                        Classification.setText(classification);
                        Log.i("classification", classification);
                        Picasso.with(getContext()).load(pictureUrl).fit().into(ItemImage);
                        break;
                    case  4:
                        Log.i("ui handler", "result " + inputMessage.getData().getString("result"));
                       // Toast.makeText(getContext(), inputMessage.getData().getString("result"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(inputMessage.getData().getString("title"));
                        builder.setMessage(inputMessage.getData().getString("result"));
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
            }
        };
        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQR(v);
            }
        });
        BarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBar(v);
            }
        });
        TextRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanText(v);
            }
        });
        Return_UnMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        if(dataGenerator == 1) {
                            returnItem();
                        }
                        else
                            updateItemState();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        });
        Borrow_Maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(dataGenerator == 1) {
                        borrowItem();
                    }
                    else
                        updateItemState();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        ConfirmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmScan(v);
            }
        });
        return view;
    }



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:


                        locationManager.requestLocationUpdates(provider, 400, 1, this);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }



    private void returnItem() throws IOException {
        Log.i("state", "returning");

        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                        + cityName + fullAddress;
        Log.i("full address", s);
        ((Student)TestJson.getUser()).returnItem(itemTag, fullAddress);
       // ((Student)TestJson.getUser()).checkBlacklistItem();
    }

    private void borrowItem() throws IOException {

        Log.i("state", "borrowing");
        if(!((Student)TestJson.getUser()).getBlacklist().equals("normal"))
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("You need to return the expired item first.");
            builder.show();
        }

        else
        {
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName + fullAddress;
            Log.i("location", s);
            Log.i("itemtag", itemTag);
            ((Student)TestJson.getUser()).borrowItem(itemTag, fullAddress);
        }
    }



    public void scanQR(View view) {

        IntentIntegrator integrator =
                new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_qr_code));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    public void scanBar(View view) {
        IntentIntegrator integrator =
                new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        // integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    public void scanText(View view) {
        Intent scan = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        startActivityForResult(scan,1);
    }

    public void confirmScan(View v){

        try {
            setItemTag(ScannedCode.getText().toString());
            Item item = new Item(itemTag);

            Log.i("itemtag is",itemTag);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ItemImage.setVisibility(View.VISIBLE);

        if(resultCode == getActivity().RESULT_FIRST_USER)
        {
            String answer = intent.getStringExtra("result");
            ScannedCode.setText(answer);

            Log.i("scan text", answer);
        }
        else{
            //retrieve scan result

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

            if (scanningResult != null) {
                codeContent = scanningResult.getContents();
                codeFormat = scanningResult.getFormatName();
                parentActivity.scanResultData(codeFormat,codeContent);
                ScannedCode.setText(codeContent);
//            try {
//                setItemTag(codeContent);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            }else{
                parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
            }

        }



    }

    public void setItemTag(String tag) throws IOException {
        itemTag = tag;

        Item item = new Item(tag);
        wv.addJavascriptInterface(item,"Item");
        item.setInfos();
    }

    public void updateItemState() throws IOException {
        Log.i("updateState", "maintain or unmaintain: "+itemTag);
        TestJson.getUser().updateItemState(itemTag);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }






}
