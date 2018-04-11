package com.example.max.testjson;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddItemFragment extends Fragment {

    private Button QR;
    private Button BarCode;
    private Button TextRecognize;

    private String codeFormat,codeContent;
    private final String noResultErrorMsg = "No scan data received!";

    public AddItemFragment() {
    }

    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
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
        return view;
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        ScanResultReceiver parentActivity = (ScanResultReceiver) this.getActivity();

        if (scanningResult != null) {


            codeContent = scanningResult.getContents();
            Toast.makeText(getActivity(),codeContent,Toast.LENGTH_SHORT).show();
            codeFormat = scanningResult.getFormatName();
            // send received data
            parentActivity.scanResultData(codeFormat,codeContent);
            studentBorrowItem(codeContent);
        }else{
            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }


    public void studentBorrowItem(String itemTag) {

        Log.i("state", "borrowing");
        TestJson.getUser().borrowItem(itemTag, "");


    }

}
