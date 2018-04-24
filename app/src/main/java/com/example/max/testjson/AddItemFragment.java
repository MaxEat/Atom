package com.example.max.testjson;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

import static com.example.max.testjson.TestJson.wv;

public class AddItemFragment extends Fragment {

    private Button QR;
    private Button BarCode;
    private Button TextRecognize;
    private Button Return_UnMaintain;
    private Button Borrow_Maintain;
    private TextView ScanResult;
    private ImageView ItemImage;

    private String itemTag;
    private String codeFormat,codeContent;
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
        return view;
    }

    private void returnItem() throws IOException {
        Log.i("state", "returning");
        TestJson.getUser().returnItem(itemTag, "");
    }

    private void borrowItem() throws IOException {

        Log.i("state", "borrowing");
        TestJson.getUser().borrowItem(itemTag, "");
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
            codeFormat = scanningResult.getFormatName();
            parentActivity.scanResultData(codeFormat,codeContent);
            try {
                setItemTag(codeContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            parentActivity.scanResultData(new NoScanResultException(noResultErrorMsg));
        }
    }

    public void setItemTag(String tag) throws IOException {
        itemTag = tag;
      //  Item item = new Item(tag);
      //  wv.addJavascriptInterface(item,"Item");
       // item.setInfos();
     //   ScanResult.setText(item.getClassification());
       // ScanResult.setText(item.getClassification()+" at " + item.getItemLocation());
    }

    public void updateItemState() throws IOException {
        Log.i("updateState", "maintain or unmaintain");
        TestJson.getUser().updateItemState(itemTag);
    }

}
