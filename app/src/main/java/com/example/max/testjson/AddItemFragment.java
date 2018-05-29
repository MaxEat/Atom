package com.example.max.testjson;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.leakcanary.RefWatcher;

import junit.framework.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.example.max.testjson.TestJson.wv;

public class AddItemFragment extends Fragment implements LocationListener{

    private LocationManager locationManager;
    private Button QR;
    private Button BarCode;
    private Button TextRecognize;
    private Button Return_UnMaintain;
    private Button Borrow_Maintain;
    private TextView ScanResult;
    private ImageView ItemImage;
    private EditText ScannedCode;
    private Button ConfirmScan;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        QR = (Button)view.findViewById(R.id.QR_scan);
        BarCode = (Button)view.findViewById(R.id.Barcode_scan);
        TextRecognize = (Button) view.findViewById(R.id.Text_scan);
        Return_UnMaintain = (Button) view.findViewById(R.id.return_item);
        Borrow_Maintain = (Button) view.findViewById(R.id.borrow_item);
        ScanResult = (TextView) view.findViewById(R.id.scan_result);
        ItemImage = (ImageView)view.findViewById(R.id.itemImage);
        ScannedCode = (EditText)view.findViewById(R.id.scannedCode) ;
        ConfirmScan = (Button)view.findViewById(R.id.confirm);

        ItemImage.setVisibility(View.INVISIBLE);
        if(dataGenerator == 1){
            Return_UnMaintain.setText("RETURN");
            Borrow_Maintain.setText("BORROW");
        }
        else{
            Return_UnMaintain.setText("UnMaintain");
            Borrow_Maintain.setText("Maintain");
        }

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

    private void returnItem() throws IOException {
        Log.i("state", "returning");
        locationManager = (LocationManager) (getContext().getSystemService(Context.LOCATION_SERVICE));

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("error", "permission not granted");
                return;
                /*------- To get city name from coordinates -------- */

            }
        String longitude = "Longitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        Log.i("longitude", longitude);
        String latitude = "Latitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        Log.i("latitude", latitude);

        String cityName = null;
        String fullAddress = null;
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                fullAddress = addresses.get(0).getAddressLine(0);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                        + cityName + fullAddress;
        Log.i("full address", s);
        ((Student)TestJson.getUser()).returnItem(itemTag, fullAddress);
        ((Student)TestJson.getUser()).checkBlacklistItem();
    }

    private void borrowItem() throws IOException {

        Log.i("state", "borrowing");
        if(((Student)TestJson.getUser()).getBlacklist().equals("normal"))
        {
            locationManager = (LocationManager) (getContext().getSystemService(Context.LOCATION_SERVICE));

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("error", "permission not granted");
                return;
            }

            String longitude = "Longitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
            Log.v("longitude", longitude);
            String latitude = "Latitude: " + locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
            Log.v("latitude", latitude);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            String fullAddress = null;
            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                    fullAddress = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                        + cityName;
            Log.i("city", s);

            ((Student)TestJson.getUser()).borrowItem(itemTag, fullAddress);
        }

        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("You need to return the expired item first.");
            builder.show();
        }
    }

    public void scanQR(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan!!");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    public void scanBar(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this.getActivity()).forSupportFragment(this); // use forSupportFragment or forFragment method to use fragments instead of activity
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt(this.getString(R.string.scan_bar_code));
        // integrator.setResultDisplayDuration(0); // milliseconds to display result on screen after scan
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void scanText(View view) {
        Intent scan = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        startActivityForResult(scan,1);
    }

    public void confirmScan(View v){

        try {
            setItemTag(ScannedCode.getText().toString());

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
        item.getImageURL();
     //   ScanResult.setText(item.getClassification());
       // ScanResult.setText(item.getClassification()+" at " + item.getItemLocation());
    }

    public void updateItemState() throws IOException {
        Log.i("updateState", "maintain or unmaintain");
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
